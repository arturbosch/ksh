package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.ShellException
import io.gitlab.arturbosch.ksh.api.CallTarget
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.loadParameterResolver
import java.lang.reflect.Method
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
class DefaultResolver : Resolver {

	override val priority: Int = -1

	private var commands: List<ShellClass> by Delegates.notNull()

	private val none = LazyThreadSafetyMode.NONE
	private val nameToProvider by lazy(none) { commands.map { it.commandId to it }.toMap() }
	private val providerToMethods by lazy(none) { commands.map { it to extractMethods(it) }.toMap() }

	private fun extractMethods(provider: ShellClass) = provider.javaClass
			.declaredMethods
			.filter { it.isAnnotationPresent(ShellMethod::class.java) }
			.map { it.toShellMethod() }

	private val parameterResolver = loadParameterResolver().first() // TODO support more

	override fun init(commands: List<ShellClass>): Resolver {
		this.commands = commands
		return this
	}

	override fun evaluate(input: String): CallTarget {
		if (input.isEmpty()) throw ShellException(null)

		val (className, raw) = destruct(input.trim())
		val provider = findMatchingClass(className)
		val (methodName, rawParameterString) = destruct(raw)
		val (methodTarget, optionless) = findMatchingMethod(provider, methodName)

		// no method specified, invoking main with optionless parameter
		val arguments = if (optionless) {
			parameterResolver.evaluate(methodTarget, "$methodName $rawParameterString")
		} else {
			parameterResolver.evaluate(methodTarget, rawParameterString)
		}

		return CallTarget(provider, methodTarget, arguments)
	}

	private fun findMatchingClass(name: String): ShellClass {
		return nameToProvider[name] ?: throw ShellException("No matching command '$name' found.")
	}

	private fun findMatchingMethod(provider: ShellClass,
								   name: String): Pair<MethodTarget, Boolean> {

		val methods = providerToMethods[provider]
				?: throw ShellException("'$provider' has no methods.")

		var searchedMethod: MethodTarget? = null
		var mainMethod: MethodTarget? = null
		for (shellMethod in methods) {
			if (shellMethod.isMain) {
				mainMethod = shellMethod
			}
			if (shellMethod.hasValue(name)) {
				searchedMethod = shellMethod
			}
		}

		return searchedMethod?.let { it to false }
				?: mainMethod?.let { it to true }
				?: throw ShellException("No sub command '$name' found for ${provider.commandId}." +
						"\n\tPossible options are: " + methods.joinToString(",") { it.name })
	}

	private fun destruct(line: String): Pair<String, String> =
			if (line.contains(SPACE)) {
				val id = line.substringBefore(SPACE)
				val remaining = line.substringAfter(SPACE)

				if (id.startsWith(OPTION_START)) {
					MAIN_METHOD_NAME to line
				} else {
					id to remaining
				}
			} else {
				when {
					line.startsWith(OPTION_START) -> MAIN_METHOD_NAME to line
					line.isEmpty() -> MAIN_METHOD_NAME to ""
					else -> line to ""
				}
			}
}

fun Method.shellMethod() = getAnnotation(ShellMethod::class.java)
		?: throw IllegalStateException("ShellMethod annotation expected.")

const val SPACE = " "
const val OPTION_START = "-"
const val MAIN_METHOD_NAME = "main"

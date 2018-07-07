package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.ShellException
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

	private val parameterResolver = loadParameterResolver().first() // TODO support more

	override fun init(commands: List<ShellClass>): Resolver {
		this.commands = commands
		return this
	}

	override fun evaluate(input: String): DefaultMethodTarget {
		if (input.isEmpty()) throw ShellException(null)

		val (className, raw) = destruct(input.trim())
		val provider = findMatchingClass(className)
		val (methodName, rawParameterString) = destruct(raw)
		val method = findMatchingMethod(provider, methodName)

		val arguments = parameterResolver.evaluate(method, rawParameterString)

		return DefaultMethodTarget(method, provider, arguments)
	}

	private fun findMatchingClass(name: String): ShellClass {
		return nameToProvider[name] ?: throw ShellException("No matching command '$name' found.")
	}

	private fun findMatchingMethod(provider: ShellClass, name: String): Method {
		val methods = providerToMethods[provider]
				?: throw ShellException("'$provider' has no methods.")

		return methods.find { name in it.shellMethod().value.toSet() || it.name == name }
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

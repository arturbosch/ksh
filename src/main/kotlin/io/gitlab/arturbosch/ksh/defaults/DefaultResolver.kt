package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.Debugging
import io.gitlab.arturbosch.ksh.ShellException
import io.gitlab.arturbosch.ksh.api.CallTarget
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.loadParameterResolver
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
open class DefaultResolver : Resolver {

	override val priority: Int = -1

	private var commands: List<ShellClass> by Delegates.notNull()

	private val nameToProvider by lazy(LazyThreadSafetyMode.NONE) {
		commands.map { it.commandId to it }.toMap()
	}
	private val providerToMethods by lazy(LazyThreadSafetyMode.NONE) {
		commands.map { it to it.extractMethods() }.toMap()
	}

	private val parameterResolver = loadParameterResolver().first() // TODO support more

	override fun init(commands: List<ShellClass>): Resolver {
		this.commands = commands
		return this
	}

	override fun evaluate(input: InputLine): CallTarget {
		val className = input.firstWord()
		val provider = findMatchingClass(className)
		val methodName = input.secondWord()
		val (methodTarget, unnamed) =
				findMatchingMethod(provider, methodName)

		val arguments = if (unnamed) {
			input.markParametersStartAfter(className)
			parameterResolver.evaluate(methodTarget, input)
		} else {
			input.markParametersStartAfter(methodName)
			parameterResolver.evaluate(methodTarget, input)
		}

		return CallTarget(provider, methodTarget, arguments).apply {
			Debugging.log { provider }
		}
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
}

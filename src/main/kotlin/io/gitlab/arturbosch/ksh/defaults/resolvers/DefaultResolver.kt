package io.gitlab.arturbosch.ksh.defaults.resolvers

import io.gitlab.arturbosch.ksh.ShellException
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.defaults.DefaultMethodTarget

/**
 * @author Artur Bosch
 */
class DefaultResolver(commands: List<ShellClass>) : Resolver(commands) {

	override val priority: Int = -1

	private val nameToProvider = commands.map { it.javaClass.simpleName to it }.toMap()
	private val providerToMethods = commands.map { it to extractMethods(it) }.toMap()

	private fun extractMethods(provider: ShellClass) = provider.javaClass.declaredMethods
			.filter { it.isAnnotationPresent(ShellMethod::class.java) }

	override fun evaluate(input: String): DefaultMethodTarget {
		if (input.isEmpty()) throw ShellException(null)

		val (className, methodName, parametersString) = destruct(input.trim())
		val provider = nameToProvider[className] ?: throw ShellException("No matching command found!")

		val methodResolver = MethodResolver(className, methodName, providerToMethods[provider] ?: emptyList())
		val (method, args) = methodResolver.evaluate(parametersString)


		return DefaultMethodTarget(method, provider, args)
	}

	private fun destruct(trimmedInput: String): Triple<String, String, String> =
			if (trimmedInput.contains(SPACE)) {
				val className = trimmedInput.substringBefore(SPACE)
				val methodAndParameters = trimmedInput.substringAfter(SPACE)

				val methodName =
						if (methodAndParameters.startsWith(OPTION_START) || methodAndParameters.isEmpty()) {
							MAIN_METHOD_NAME
						} else {
							methodAndParameters.substringBefore(SPACE)
						}

				Triple(className, methodName, methodAndParameters.substringAfter(SPACE))
			} else {
				Triple(trimmedInput, MAIN_METHOD_NAME, "")
			}
}

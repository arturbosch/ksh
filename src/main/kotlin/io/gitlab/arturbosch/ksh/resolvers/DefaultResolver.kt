package io.gitlab.arturbosch.ksh.resolvers

import io.gitlab.arturbosch.ksh.api.CommandProvider
import io.gitlab.arturbosch.ksh.api.ShellMethod

/**
 * @author Artur Bosch
 */
class DefaultResolver(commands: List<CommandProvider>) : Resolver(commands) {

	override val priority: Int = -1

	private val nameToProvider = commands.map { it.javaClass.simpleName to it }.toMap()
	private val providerToMethods = commands.map { it to extractMethods(it) }.toMap()

	private fun extractMethods(provider: CommandProvider) = provider.javaClass.declaredMethods
			.filter { it.isAnnotationPresent(ShellMethod::class.java) }

	private val SPACE = " "

	override fun evaluate(input: String?): MethodTarget? {
		if (input == null || input.isBlank()) return null
		val trimmedInput = input.trim()

		val (className, methodName, parametersString) = destruct(trimmedInput)
		val provider = nameToProvider[className]

		val methodResolver = MethodResolver(methodName, providerToMethods[provider] ?: emptyList())
		val (method, args) = methodResolver.evaluate(parametersString)

		return MethodTarget.of(method, provider, args)
	}

	private fun destruct(trimmedInput: String): Triple<String, String, String> = if (trimmedInput.contains(SPACE)) {
		val className = trimmedInput.substringBefore(SPACE)
		val methodAndParameters = trimmedInput.substringAfter(SPACE)

		val methodName = if (methodAndParameters.startsWith("--") || methodAndParameters.isEmpty()) {
			"main"
		} else {
			methodAndParameters.substringBefore(SPACE)
		}

		Triple(className, methodName, methodAndParameters.substringAfter(SPACE))
	} else {
		Triple(trimmedInput, "main", "")
	}
}

package io.gitlab.arturbosch.ksh

/**
 * @author Artur Bosch
 */
interface Resolver {
	fun evaluate(input: String?)
}

class DefaultResolver(commands: List<CommandProvider>) : Resolver {

	val nameToProvider = commands.map { it.javaClass.simpleName to it }.toMap()
	val providerToMethods = commands.map { it to extractMethods(it) }.toMap()

	private fun extractMethods(provider: CommandProvider) = provider.javaClass.declaredMethods
			.filter { it.isAnnotationPresent(Command::class.java) }

	private val SPACE = " "

	override fun evaluate(input: String?) {
		if (input == null || input.isBlank()) return

		val trimmedInput = input.trim()
		val className = trimmedInput.substringBefore(SPACE)
		val methodNameAndParameters = trimmedInput.substringAfter(SPACE)

		val methodName = if (methodNameAndParameters.startsWith("--")) {
			"main"
		} else {
			methodNameAndParameters.substringBefore(SPACE)
		}

		val provider = nameToProvider[className]
		val method = providerToMethods[provider]?.find { it.name == methodName }
		val result = method?.invoke(provider, *arrayOf()) ?: ShellException("No matching command found for $input")
		println(result)
	}
}
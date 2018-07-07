package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.ShellException
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.defaults.resolvers.MAIN_METHOD_NAME
import io.gitlab.arturbosch.ksh.defaults.resolvers.MethodResolver
import io.gitlab.arturbosch.ksh.defaults.resolvers.OPTION_START
import io.gitlab.arturbosch.ksh.defaults.resolvers.SPACE
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

	override fun init(commands: List<ShellClass>): Resolver {
		this.commands = commands
		return this
	}

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

				val (methodName, rawParameters) =
						if (methodAndParameters.startsWith(OPTION_START) || methodAndParameters.isEmpty()) {
							MAIN_METHOD_NAME to methodAndParameters
						} else {
							methodAndParameters.substringBefore(SPACE) to methodAndParameters.substringAfter(SPACE)
						}

				Triple(className, methodName, rawParameters)
			} else {
				Triple(trimmedInput, MAIN_METHOD_NAME, "")
			}
}

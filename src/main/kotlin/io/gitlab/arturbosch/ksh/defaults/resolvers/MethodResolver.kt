package io.gitlab.arturbosch.ksh.defaults.resolvers

import com.beust.jcommander.JCommander
import io.gitlab.arturbosch.ksh.ShellException
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
class MethodResolver(private val className: String,
					 private val methodName: String,
					 private val methods: List<Method>) {

	private val paramError =
			"Command '$className $methodName' must provide zero or " +
					"only one parameter which can be parsed by JCommander!"

	fun evaluate(input: String): MethodSignature {
		val method = methods.find { it.name == methodName }
		method ?: throw ShellException("No sub command '$methodName' found for $className." +
				"\n\tPossible options are: " + methods.joinToString(",") { it.name })

		val parameters = method.parameters
		val hasOneParameter = parameters.size == 1
		require(parameters.isEmpty() || hasOneParameter) { paramError }

		val arguments = if (hasOneParameter) {
			val type = parameters[0].type
			val instance = type.newInstance()
			JCommander(instance).parse(input)
			listOf(instance)
		} else {
			emptyList()
		}

		return MethodSignature(method, arguments)
	}

}

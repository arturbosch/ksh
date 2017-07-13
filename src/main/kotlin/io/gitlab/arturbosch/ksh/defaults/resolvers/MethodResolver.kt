package io.gitlab.arturbosch.ksh.defaults.resolvers

import com.beust.jcommander.JCommander
import io.gitlab.arturbosch.ksh.ShellException
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
class MethodResolver(val className: String,
					 val methodName: String,
					 val methods: List<Method>) {

	private val PARAMETER_ERROR =
			"Command '$className $methodName' must provide zero or " +
					"only one parameter which can be parsed by JCommander!"

	fun evaluate(input: String): MethodSignature {
		val method = methods.find { it.name == methodName }
		method ?: throw ShellException("No sub command '$methodName' found for $className." +
				"\n\tPossible options are: " + methods.joinToString(",") { it.name })

		val parameters = method.parameters
		val hasOneParameter = parameters.size == 1
		require(parameters.isEmpty() || hasOneParameter) { PARAMETER_ERROR }

		val arguments = if (hasOneParameter) {
			val type = parameters[0].type
			val instance = type.newInstance()
			JCommander(instance).parse(input)
			arrayOf(instance)
		} else {
			emptyArray<Any>()
		}

		return MethodSignature(method, arguments)
	}

}
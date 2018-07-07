package io.gitlab.arturbosch.ksh.defaults.resolvers

import io.gitlab.arturbosch.ksh.ShellException
import io.gitlab.arturbosch.ksh.defaults.DefaultParameterResolver
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
class MethodResolver(private val className: String,
					 private val methodName: String,
					 private val methods: List<Method>) {

	fun evaluate(parameters: String): MethodSignature {
		val method = methods.find { it.name == methodName }
		method ?: throw ShellException("No sub command '$methodName' found for $className." +
				"\n\tPossible options are: " + methods.joinToString(",") { it.name })

		val arguments = DefaultParameterResolver()
				.evaluate(method, parameters)

		return MethodSignature(method, arguments)
	}
}

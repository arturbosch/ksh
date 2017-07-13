package io.gitlab.arturbosch.ksh.defaults.resolvers

import io.gitlab.arturbosch.ksh.ShellException
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
class MethodResolver(val className: String,
					 val methodName: String,
					 val methods: List<Method>) {

	fun evaluate(input: String): MethodSignature {
		val method = methods.find { it.name == methodName }
		method ?: throw ShellException("No sub command '$methodName' found for $className." +
				"\n\tPossible options are: " + methods.joinToString(",") { it.name })
		return MethodSignature(method)
	}

}
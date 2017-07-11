package io.gitlab.arturbosch.ksh.resolvers

import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
class MethodResolver(val methodName: String,
					 val methods: List<Method>) {

	fun evaluate(input: String?): MethodSignature {
		val method = methods.find { it.name == methodName }
		return MethodSignature(method)
	}

}
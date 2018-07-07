package io.gitlab.arturbosch.ksh.api

import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
interface ParameterResolver : WithPriority {

	fun supports(parameter: Parameter): Boolean
	fun evaluate(method: Method, rawParameterInput: String): List<Any?>
}

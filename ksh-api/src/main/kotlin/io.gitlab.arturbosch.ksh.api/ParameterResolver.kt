package io.gitlab.arturbosch.ksh.api

import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
interface ParameterResolver : WithPriority {

	fun supports(parameter: Parameter): Boolean
	fun evaluate(methodTarget: MethodTarget, input: InputLine): List<Any?>
}

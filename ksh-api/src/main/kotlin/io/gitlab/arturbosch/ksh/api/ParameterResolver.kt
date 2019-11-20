package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority

/**
 * @author Artur Bosch
 */
interface ParameterResolver : WithPriority {

    @Throws(UnsupportedParameter::class)
    fun supports(methodTarget: MethodTarget): Boolean

    fun evaluate(methodTarget: MethodTarget, input: InputLine): List<Any?>
}

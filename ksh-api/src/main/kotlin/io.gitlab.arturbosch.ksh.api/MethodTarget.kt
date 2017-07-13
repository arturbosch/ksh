package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface MethodTarget {

	fun invoke(): Any?
}
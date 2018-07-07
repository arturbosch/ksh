package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
@FunctionalInterface
interface Converter<T> {
	fun convert(input: String): T
}

package io.gitlab.arturbosch.ksh.api

import kotlin.reflect.KClass

/**
 * @author Artur Bosch
 */
interface Converter<T : Any> : WithPriority {

	val id: KClass<T>
	fun parse(input: String): T
}

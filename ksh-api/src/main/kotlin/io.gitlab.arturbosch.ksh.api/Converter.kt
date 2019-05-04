package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority
import kotlin.reflect.KClass

/**
 * @author Artur Bosch
 */
interface Converter<T : Any> : WithPriority {

    val id: KClass<T>
    fun parse(input: String): T
}

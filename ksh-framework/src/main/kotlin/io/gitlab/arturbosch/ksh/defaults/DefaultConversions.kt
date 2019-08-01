package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.ShellOption
import java.lang.reflect.Parameter
import kotlin.reflect.KClass

/**
 * @author Artur Bosch
 */
class DefaultConversions(converts: List<Converter<*>>) {

    private val converters: Map<KClass<*>, Converter<*>> =
        converts.asSequence()
            .sortedBy { it.priority }
            .map { it.id to it }
            .toMap()

    fun supports(parameter: Parameter) = converters.containsKey(parameter.type.kotlin)

    fun convert(parameter: Parameter, argument: String): Any? {
        if (argument == ShellOption.NULL_DEFAULT) {
            return if (parameter.hasBoolType()) false else null
        }
        val converter = converters[parameter.type.kotlin]
        if (converter != null) {
            return converter.parse(argument)
        }
        throw IllegalArgumentException("No converter found for type '${parameter.type}' and argument '$argument'.")
    }
}

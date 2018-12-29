package io.gitlab.arturbosch.ksh.converters

import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.ksh.kshLoader
import java.lang.reflect.Parameter
import java.util.ServiceLoader
import kotlin.reflect.KClass

/**
 * @author Artur Bosch
 */
class Conversions {

    private val converters: Map<KClass<*>, Converter<*>> =
            ServiceLoader.load(Converter::class.java, kshLoader).toList()
                    .asSequence()
                    .sortedBy { it.priority }
                    .map { it.id to it }
                    .toMap()

    fun supports(parameter: Parameter) = converters.containsKey(parameter.type.kotlin)

    fun convert(parameter: Parameter, argument: String): Any? {
        if (argument == ShellOption.NULL_DEFAULT) {
            return null
        }
        val converter = converters[parameter.type.kotlin]
        if (converter != null) {
            return converter.parse(argument)
        }
        throw IllegalArgumentException("Could not convert '$argument' to '${parameter.type}'.")
    }
}

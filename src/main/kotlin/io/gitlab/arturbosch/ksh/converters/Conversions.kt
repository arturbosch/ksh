package io.gitlab.arturbosch.ksh.converters

import io.gitlab.arturbosch.ksh.api.ShellOptions
import java.io.File
import java.lang.reflect.Parameter

private val booleans = setOf(Boolean::class.javaPrimitiveType, Boolean::class.java)
private val ints = setOf(Int::class.javaPrimitiveType, Int::class.java)
private val floats = setOf(Float::class.javaPrimitiveType, Float::class.java)
private val doubles = setOf(Double::class.javaPrimitiveType, Double::class.java)

/**
 * @author Artur Bosch
 */
fun convert(parameter: Parameter, argument: String): Any? {
	if (argument == ShellOptions.NULL_DEFAULT) {
		return null
	}
	return when (parameter.type) {
		String::class.java -> StringConverter(argument)
		File::class.java -> FileConverter(argument)
		in booleans -> BoolConverter(argument)
		in ints -> IntConverter(argument)
		in floats -> FloatConverter(argument)
		in doubles -> DoubleConverter(argument)
		else -> throw IllegalArgumentException("Could not convert '$argument' to '${parameter.type}'.")
	}
}

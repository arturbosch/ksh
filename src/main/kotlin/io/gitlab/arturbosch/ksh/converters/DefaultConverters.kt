package io.gitlab.arturbosch.ksh.converters

import io.gitlab.arturbosch.ksh.api.Converter
import java.io.File

/**
 * @author Artur Bosch
 */
typealias KotlinConverter<T> = (String) -> T

val IntConverter: KotlinConverter<Int> = { input: String -> input.toInt() }
val StringConverter: KotlinConverter<String> = { input: String -> input }
val BoolConverter: KotlinConverter<Boolean> = { input: String -> input.toBoolean() }
val DoubleConverter: KotlinConverter<Double> = { input: String -> input.toDouble() }
val FloatConverter: KotlinConverter<Float> = { input: String -> input.toFloat() }
val FileConverter: KotlinConverter<File> = { input: String -> File(input) }

fun <T> handle(converter: KotlinConverter<T>) = object : Converter<T> {
	override fun convert(input: String): T = converter(input)
}

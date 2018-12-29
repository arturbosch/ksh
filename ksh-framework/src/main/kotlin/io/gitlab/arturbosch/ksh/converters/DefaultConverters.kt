package io.gitlab.arturbosch.ksh.converters

import io.gitlab.arturbosch.ksh.api.Converter
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

class StringConverter : Converter<String> {
    override val id: KClass<String> = String::class
    override fun parse(input: String): String = input
}

class IntConverter : Converter<Int> {
    override val id: KClass<Int> = Int::class
    override fun parse(input: String): Int = input.toInt()
}

class BoolConverter : Converter<Boolean> {
    override val id: KClass<Boolean> = Boolean::class
    override fun parse(input: String): Boolean = input.toBoolean()
}

class FloatConverter : Converter<Float> {
    override val id: KClass<Float> = Float::class
    override fun parse(input: String): Float = input.toFloat()
}

class DoubleConverter : Converter<Double> {
    override val id: KClass<Double> = Double::class
    override fun parse(input: String): Double = input.toDouble()
}

class FileConverter : Converter<File> {
    override val id: KClass<File> = File::class
    override fun parse(input: String): File = File(input)
}

class PathConverter : Converter<Path> {
    override val id: KClass<Path> = Path::class
    override fun parse(input: String): Path = Paths.get(input)
}

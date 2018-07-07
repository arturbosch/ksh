package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.ksh.api.ShellOptions
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
class DefaultParameterResolver {

	fun evaluate(method: Method, rawParameterInput: String): List<Any> {
		val prefix = method.parameterPrefix()
		val allKeys = method.parameters.flatMap { it.prefixedValues(prefix) }

		val methodParameters: MutableMap<Parameter, MethodParameter> = mutableMapOf()
		val words = rawParameterInput.split(" ")
		val unusedWords = mutableSetOf<String>()
		for ((index, word) in words.withIndex()) {
			if (word in allKeys) {
				val parameter = lookupParameter(method, word, prefix)
				val arity = parameter.arity()
				val from = index + 1
				val to = from + arity
				val values = words.subList(from, to)
				methodParameters[parameter] = MethodParameter(word, values, parameter)
			} else {
				unusedWords.add(word)
			}
		}

		val arguments = mutableListOf<Any>()
		for (parameter in method.parameters) {
			val methodParameter = methodParameters[parameter]
			if (methodParameter != null) {
				val (_, values, _) = methodParameter
				val argument: Any = when (values.size) {
					0 -> true
					1 -> values[0]
					else -> values
				}
				arguments.add(argument)
			}
		}

		return arguments
	}

	private fun lookupParameter(method: Method, word: String, prefix: String): Parameter {
		for (parameter in method.parameters) {
			if (parameter.prefixedValues(prefix).any { it == word }) {
				return parameter
			}
		}
		throw IllegalArgumentException("Could not look up parameter for '$prefix$word' in $method")
	}
}

data class MethodParameter(val option: String,
						   val values: List<String>,
						   val parameter: Parameter)

fun Method.parameterPrefix() = getAnnotation(ShellMethod::class.java)?.prefix
		?: throw IllegalStateException("Method with ShellMethod annotation expected.")

fun Parameter.arity(): Int {
	val option = getAnnotation(ShellOption::class.java)
	val inferred = if (hasBoolType()) 0 else 1
	return if (option != null && option.arity != ShellOptions.EXTRACT_ARITY) option.arity else inferred
}

fun Parameter.hasBoolType() =
		type == Boolean::class.javaPrimitiveType || type == Boolean::class.java

fun Parameter.prefixedValues(prefix: String): Set<String> {
	val option = getAnnotation(ShellOption::class.java)
	return if (option != null && option.value.isNotEmpty()) {
		option.value.toSet()
	} else {
		setOf(prefix + name)
	}
}

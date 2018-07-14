package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.converters.convert
import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
class DefaultParameterResolver : ParameterResolver {

	data class MethodParameter(val option: String,
							   val values: List<String>,
							   val parameter: Parameter)

	override fun supports(parameter: Parameter): Boolean = true

	override fun evaluate(methodTarget: MethodTarget, input: InputLine): List<Any?> {
		val prefix = methodTarget.method.parameterPrefix()
		val allKeys = methodTarget.parameters.flatMap { it.prefixedValues(prefix) }

		val methodParameters: MutableMap<Parameter, MethodParameter> = mutableMapOf()
		val words = input.words().subList(input.parameterStartIndex, input.size())
		val unusedWords = mutableSetOf<String>()
		for ((index, word) in words.withIndex()) {
			if (word in allKeys) {
				val parameter = methodTarget.lookupParameter(word, prefix)
				val arity = parameter.arity()
				val from = index + 1
				val to = from + arity
				val values = words.subList(from, to)
				methodParameters[parameter] = MethodParameter(word, values, parameter)
			} else {
				unusedWords.add(word)
			}
		}

		val arguments = mutableListOf<Any?>()
		for (parameter in methodTarget.parameters) {
			val methodParameter = methodParameters[parameter]
			if (methodParameter != null) {
				val (_, values, _) = methodParameter
				val argument: String = when (values.size) {
					0 -> "true"
					1 -> values[0]
					else -> values.joinToString("; ")
				}
				val convertedArgument = convert(parameter, argument)
				arguments.add(convertedArgument)
			} else {
				val convertedArgument =
						if (unusedWords.isNotEmpty() && parameter.isUnnamedOption()) {
							val word = unusedWords.first()
							unusedWords.remove(word)
							convert(parameter, word)
						} else {
							convert(parameter, parameter.defaultValue())
						}
				arguments.add(convertedArgument)
			}
		}

		return arguments
	}
}

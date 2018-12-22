package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.Debugging
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.converters.Conversions
import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
class DefaultParameterResolver : ParameterResolver {

    private val converter = Conversions()

    data class MethodParameter(
        val option: String,
        val values: List<String>,
        val parameter: Parameter
    )

    override fun supports(parameter: Parameter): Boolean = converter.supports(parameter)

    override fun evaluate(methodTarget: MethodTarget, input: InputLine): List<Any?> {
        val prefix = methodTarget.parameterPrefix()
        val allKeys = methodTarget.parameters.flatMap { it.prefixedValues(prefix) }

        val methodParameters: MutableMap<Parameter, MethodParameter> = mutableMapOf()
        val words = input.words().subList(input.parameterStartIndex, input.size())
        val unusedWords = mutableSetOf<String>()
        var nextIndex = 0
        for ((index, word) in words.withIndex()) {
            if (index < nextIndex) {
                continue
            }
            if (word in allKeys) {
                val parameter = methodTarget.lookupParameter(word, prefix)
                val arity = parameter.arity()
                val from = index + 1
                val to = from + arity
                val values = words.subList(from, to)
                methodParameters[parameter] = MethodParameter(word, values, parameter)
                nextIndex = to
            } else {
                unusedWords.add(word)
            }
        }

        // special case: method with one parameter which may be unnamed
        // concatenate all unused word so basically e.g. test one two.. becomes test "one two..".
        if (methodTarget.parameters.size == 1 && unusedWords.isNotEmpty()) {
            val firstParam = methodTarget.parameters[0]
            if (firstParam.isUnnamedOption()) {
                val fullArgument = unusedWords.joinToString(" ")
                val converted = converter.convert(firstParam, fullArgument)
                return listOf(converted)
            }
        }

        val arguments = mutableListOf<Any?>()

        for (parameter in methodTarget.parameters) {
            val methodParameter = methodParameters[parameter]
            val convertedArgument = if (methodParameter != null) {
                val (_, values, _) = methodParameter
                val argument: String = when (values.size) {
                    0 -> "true"
                    1 -> values[0]
                    else -> values.joinToString("; ")
                }
                converter.convert(parameter, argument)
            } else {
                if (unusedWords.isNotEmpty() && parameter.isUnnamedOption()) {
                    val word = unusedWords.first()
                    unusedWords.remove(word)
                    converter.convert(parameter, word)
                } else {
                    converter.convert(parameter, parameter.defaultValue())
                }
            }
            Debugging.log(convertedArgument)
            arguments.add(convertedArgument)
        }

        return arguments
    }
}

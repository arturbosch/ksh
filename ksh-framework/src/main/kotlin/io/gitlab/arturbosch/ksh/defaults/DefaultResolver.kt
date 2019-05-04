package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.Debugging
import io.gitlab.arturbosch.ksh.api.CallTarget
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellException
import io.gitlab.arturbosch.kutils.toHashMap

/**
 * @author Artur Bosch
 */
open class DefaultResolver(
    private val parameterResolvers: List<ParameterResolver>
) : Resolver {

    override val priority: Int = -1

    private lateinit var nameToProvider: Map<String, ShellClass>
    private lateinit var providerToMethods: Map<ShellClass, List<MethodTarget>>

    override fun init(commands: List<ShellClass>): Resolver {
        nameToProvider = commands.toHashMap({ it.commandId }, { it })
        providerToMethods = commands.toHashMap({ it }, { it.extractMethods() })
        return this
    }

    override fun evaluate(input: InputLine): CallTarget {
        val className = input.firstWord()
        val provider = findMatchingClass(className)
        val methodName = input.secondWord()
        val (methodTarget, unnamed) =
            findMatchingMethod(provider, methodName)

        val parameterResolver = parameterResolvers.first { it.supports(methodTarget) }

        val arguments = if (unnamed) {
            input.markParametersStartAfter(className)
            parameterResolver.evaluate(methodTarget, input)
        } else {
            input.markParametersStartAfter(methodName)
            parameterResolver.evaluate(methodTarget, input)
        }

        Debugging.log { provider }
        return CallTarget(provider, methodTarget, arguments)
    }

    private fun findMatchingClass(name: String): ShellClass {
        return nameToProvider[name] ?: throw ShellException("No matching command '$name' found.")
    }

    private fun findMatchingMethod(
        provider: ShellClass,
        name: String
    ): Pair<MethodTarget, Boolean> {

        val methods = providerToMethods[provider]
            ?: throw ShellException("'$provider' has no methods.")

        val searchedMethod: MethodTarget? = methods.firstOrNull { it.hasValue(name) }

        return searchedMethod?.let { it to false }
            ?: methods.firstOrNull { it.isMain }?.let { it to true }
            ?: throw ShellException("No sub command '$name' found for ${provider.commandId}." +
                "\n\tPossible options are: " + methods.joinToString(",") { it.name })
    }
}

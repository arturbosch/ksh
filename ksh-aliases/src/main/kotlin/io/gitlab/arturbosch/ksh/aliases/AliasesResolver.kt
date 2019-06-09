package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.ksh.api.CallTarget
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellException
import io.gitlab.arturbosch.ksh.api.SimpleInputLine

class AliasesResolver(
    private val aliasMapping: Map<String, String>
) : Resolver {

    override val priority: Int = 100 // try to be called first

    override fun init(commands: List<ShellClass>) = Unit
    override fun supports(input: InputLine): Boolean = false
    override fun transforms(input: InputLine): Boolean = input.containsAlias()

    override fun transform(input: InputLine): InputLine {
        val lookup = input.words().joinToString(" ")
        val transformed = aliasMapping[lookup] ?: throw ShellException("No alias found for '$input'")
        return SimpleInputLine(transformed)
    }

    fun InputLine.containsAlias(): Boolean = aliasMapping[words().joinToString("")] != null

    override fun evaluate(input: InputLine): CallTarget =
        throw UnsupportedOperationException("evaluation unsupported")
}

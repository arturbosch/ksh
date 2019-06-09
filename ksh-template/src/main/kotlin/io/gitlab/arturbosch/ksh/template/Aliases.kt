package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.aliases.AliasesProvider
import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.resourceAsStream

class Aliases : AliasesProvider {

    override fun provide(container: Container): Map<String, String> {
        return resourceAsStream("aliases.properties")
            .bufferedReader()
            .useLines { it.map(this::lineToAlias).toMap() }
    }

    private fun lineToAlias(line: String): Pair<String, String> {
        val (key, value) = line.split("=")
        return key to value
    }
}

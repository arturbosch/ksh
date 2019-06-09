package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.ksh.api.provider.Provider
import io.gitlab.arturbosch.kutils.Container

interface AliasesProvider : Provider<Map<String, String>>

class EmptyAliasesProvider : AliasesProvider {

    override fun provide(container: Container): Map<String, String> = emptyMap()
}

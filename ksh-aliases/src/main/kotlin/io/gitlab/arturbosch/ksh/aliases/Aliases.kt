package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.firstPrioritized
import io.gitlab.arturbosch.kutils.load

fun aliases(container: Injektor): Map<String, String> =
    load<AliasesProvider>()
        .firstPrioritized()
        ?.provide(container)
        ?: EmptyAliasesProvider().provide(container)

package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.provider.ResolverProvider
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.firstPrioritized
import io.gitlab.arturbosch.kutils.load

class AliasesResolverProvider : ResolverProvider {

    override fun provide(container: Injektor): Resolver {
        val aliases = load<AliasesProvider>()
            .firstPrioritized()
            ?.provide(container)
            ?: EmptyAliasesProvider().provide(container)
        return AliasesResolver(aliases)
    }
}

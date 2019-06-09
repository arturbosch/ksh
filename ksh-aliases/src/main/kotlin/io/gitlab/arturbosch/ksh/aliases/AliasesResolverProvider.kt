package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.provider.ResolverProvider
import io.gitlab.arturbosch.kutils.Injektor

class AliasesResolverProvider : ResolverProvider {

    override fun provide(container: Injektor): Resolver = AliasesResolver(aliases(container))
}

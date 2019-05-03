package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.provider.ParameterResolverProvider
import io.gitlab.arturbosch.ksh.api.provider.ResolverProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultResolver
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.load
import io.gitlab.arturbosch.kutils.withSingleton

class DefaultResolverProvider : ResolverProvider {

    override fun provide(container: Injektor): Resolver {
        val resolvers = load<ParameterResolverProvider>()
            .map { it.provide(container) }
            .sortedBy { it.priority }
            .reversed()
            .toList()
        return container.withSingleton(DefaultResolver(resolvers))
    }
}

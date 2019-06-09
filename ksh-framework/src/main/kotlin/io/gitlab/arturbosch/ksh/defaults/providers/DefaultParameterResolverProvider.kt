package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.api.provider.ConvertersProvider
import io.gitlab.arturbosch.ksh.api.provider.ParameterResolverProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultConversions
import io.gitlab.arturbosch.ksh.defaults.DefaultParameterResolver
import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.load
import io.gitlab.arturbosch.kutils.withSingleton

class DefaultParameterResolverProvider : ParameterResolverProvider, WithLowPriority {

    override fun provide(container: Container): ParameterResolver {
        val converters = load<ConvertersProvider>().flatMap { it.provide(container) }
        val conversions = container.withSingleton(DefaultConversions(converters))
        return container.withSingleton(DefaultParameterResolver(conversions))
    }
}

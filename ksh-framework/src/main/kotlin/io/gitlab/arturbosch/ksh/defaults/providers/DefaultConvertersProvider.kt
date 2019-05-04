package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.provider.ConvertersProvider
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.load

class DefaultConvertersProvider : ConvertersProvider {

    override fun provide(container: Injektor): List<Converter<*>> {
        return load<Converter<*>>().toList()
    }
}

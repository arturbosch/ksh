package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.provider.ConvertersProvider
import io.gitlab.arturbosch.ksh.kshLoader
import io.gitlab.arturbosch.kutils.Injektor
import java.util.ServiceLoader

class DefaultConvertersProvider : ConvertersProvider {

    override fun provide(container: Injektor): List<Converter<*>> {
        return ServiceLoader.load(Converter::class.java, kshLoader).toList()
    }
}

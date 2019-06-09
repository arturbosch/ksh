package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.provider.ConvertersProvider
import io.gitlab.arturbosch.ksh.defaults.BoolConverter
import io.gitlab.arturbosch.ksh.defaults.DoubleConverter
import io.gitlab.arturbosch.ksh.defaults.FileConverter
import io.gitlab.arturbosch.ksh.defaults.FloatConverter
import io.gitlab.arturbosch.ksh.defaults.IntConverter
import io.gitlab.arturbosch.ksh.defaults.PathConverter
import io.gitlab.arturbosch.ksh.defaults.StringConverter
import io.gitlab.arturbosch.kutils.Container

class DefaultConvertersProvider : ConvertersProvider, WithLowPriority {

    override fun provide(container: Container): List<Converter<*>> =
        listOf(
            StringConverter(),
            IntConverter(),
            BoolConverter(),
            FloatConverter(),
            DoubleConverter(),
            FileConverter(),
            PathConverter()
        )
}

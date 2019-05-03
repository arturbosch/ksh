package io.gitlab.arturbosch.ksh.converters

import assertk.assertions.each
import io.gitlab.arturbosch.ksh.NoopContainer
import io.gitlab.arturbosch.ksh.defaults.DefaultConversions
import io.gitlab.arturbosch.ksh.defaults.providers.DefaultConvertersProvider
import org.junit.Test
import java.io.File
import java.lang.reflect.Parameter
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class ConverterTest {

    @Suppress("unused", "UNUSED_PARAMETER")
    fun parameters(path: Path, file: File) = Unit

    @Test
    fun defaultConvertersWork() {
        val conversions = DefaultConversions(DefaultConvertersProvider().provide(NoopContainer()))

        assertk.assert {
            ConverterTest::class.java.getDeclaredMethod("parameters", Path::class.java, File::class.java)
                .parameters
                .map { it to associateBy(it) }
                .map { conversions.convert(it.first, it.second) }
        }.returnedValue { each { checkNotNull(it.actual)::class in setOf(Path::class, File::class) } }
    }

    private fun associateBy(it: Parameter): String {
        return when (it.type) {
            Boolean::class.java -> "true"
            Int::class.java -> "5"
            Double::class.java -> "5.0"
            File::class.java -> "/path/to/file"
            Path::class.java -> "/path/to/file"
            else -> throw IllegalStateException("no")
        }
    }
}

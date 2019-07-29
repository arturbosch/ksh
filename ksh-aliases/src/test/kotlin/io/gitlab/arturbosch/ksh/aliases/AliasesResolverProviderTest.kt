package io.gitlab.arturbosch.ksh.aliases

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.gitlab.arturbosch.ksh.NoopContainer
import io.gitlab.arturbosch.ksh.api.SimpleInputLine
import io.gitlab.arturbosch.kutils.Container
import org.junit.Test

class AliasesResolverProviderTest {

    @Test
    fun `resolves aliases`() {
        val resolver = AliasesResolverProvider().provide(NoopContainer())
        val transformed = resolver.transform(SimpleInputLine("hello"))
        assertThat(transformed.words()).isEqualTo(listOf("help", "hello"))
    }
}

class TestAliasesProvider : AliasesProvider {

    override val priority: Int = 100
    override fun provide(container: Container): Map<String, String> {
        return hashMapOf("hello" to "help hello")
    }
}

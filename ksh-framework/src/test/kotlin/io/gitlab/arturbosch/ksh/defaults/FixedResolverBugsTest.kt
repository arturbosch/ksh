package io.gitlab.arturbosch.ksh.defaults

import assertk.assertions.isEqualTo
import io.gitlab.arturbosch.ksh.Gradle
import io.gitlab.arturbosch.ksh.test.testResolver
import org.junit.Test

/**
 * @author Artur Bosch
 */
class FixedResolverBugsTest {

    private val resolver = testResolver(Gradle())

    @Test
    fun `should skip non flag words after arity is resolved`() {
        val target = resolver.evaluate("gradle module testy -wd /bla/bla -g a.b.c")
        assertk.assert(target).isEqualTo("")
    }
}

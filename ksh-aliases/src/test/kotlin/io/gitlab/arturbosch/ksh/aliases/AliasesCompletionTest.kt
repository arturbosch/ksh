package io.gitlab.arturbosch.ksh.aliases

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import org.jline.reader.Candidate
import org.jline.reader.impl.DefaultParser
import org.junit.Test

class AliasesCompletionTest {

    private val parser = DefaultParser()

    @Test
    fun `completes aliases`() {
        val completer = AliasesCompleter(setOf("hello", "bye"))

        setOf("hel", "bye").forEach { case ->
            val line = parser.parse(case, (case.length - 1).takeIf { it >= 0 } ?: 0)
            val candidates = mutableListOf<Candidate>()

            completer.complete(null, line, candidates)

            assertThat(candidates).hasSize(1)
        }

        setOf("helo", "byeo").forEach { case ->
            val line = parser.parse(case, (case.length - 1).takeIf { it >= 0 } ?: 0)
            val candidates = mutableListOf<Candidate>()

            completer.complete(null, line, candidates)

            assertThat(candidates).isEmpty()
        }
    }
}

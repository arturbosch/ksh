package io.gitlab.arturbosch.ksh.defaults

import assertk.assertThat
import assertk.assertions.containsAll
import io.gitlab.arturbosch.ksh.Conversions
import io.gitlab.arturbosch.ksh.Hello
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.test.testContext
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.impl.DefaultParser
import org.junit.Test

/**
 * @author Artur Bosch
 */
class DefaultCompleterTest {

    class DefaultCompleterWrapper(commands: List<ShellClass>) {
        private val completer: Completer = DefaultCompleter().apply { init(commands) }
        private val parser = DefaultParser()
        private val context = testContext()

        fun complete(currentLine: String): List<String> {
            val line = parser.parse(currentLine, (currentLine.length - 1).takeIf { it >= 0 } ?: 0)
            val candidates = mutableListOf<Candidate>()
            completer.complete(context.reader, line, candidates)
            return candidates.map { it.value() }
        }
    }

    private val commandsUnderTest = listOf(Hello(), Conversions())
    private val completer = DefaultCompleterWrapper(commandsUnderTest)

    @Test
    fun `completion of all shell classes if nothing is entered`() {
        val candidates = completer.complete("")
        assertThat(candidates).containsAll("hello", "conversions")
    }

    @Test
    fun `completion of hello command when only word is hel`() {
        val candidates = completer.complete("hel")
        assertThat(candidates).containsAll("hello")
    }

    @Test
    fun `completion of sub commands of hello are shown`() {
        val candidates = completer.complete("hello  ")
        assertThat(candidates).containsAll("say", "count", "invalid")
    }

    @Test
    fun `completion of main command's (hello) only option is shown as second word indicates an option`() {
        val candidates = completer.complete("hello --")
        assertThat(candidates).containsAll("--name", "-n")
    }

    @Test
    fun `completion of sub commands options when third word is empty`() {
        val candidates = completer.complete("hello count  ")
        assertThat(candidates).containsAll("--name", "-n", "--times")
    }

    @Test
    fun `completion of sub commands options when third word indicates an option`() {
        val candidates = completer.complete("hello count -")
        assertThat(candidates).containsAll("--name", "-n", "--times")
    }

    @Test
    fun `completion of sub commands options when third word indicates a long option`() {
        val candidates = completer.complete("hello count --")
        assertThat(candidates).containsAll("--name", "--times")
    }

    @Test
    fun `completion of sub commands option 'times'`() {
        val candidates = completer.complete("hello count --tim")
        assertThat(candidates).containsAll("--times")
    }
}

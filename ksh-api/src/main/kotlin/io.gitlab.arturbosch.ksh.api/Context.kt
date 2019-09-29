package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.WithPriority
import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
interface Context : WithPriority {

    val container: Container
    val settings: ShellSettings
    val reader: LineReader
    val terminal: Terminal
    val resolvers: List<Resolver>
    var lastExceptionState: Throwable?

    fun commands(): List<ShellClass>

    @JvmDefault
    fun writeln(msg: String?) = terminal.writer().write(msg + "\n")

    @JvmDefault
    fun writeln(error: Throwable?) = error?.printStackTrace(terminal.writer())

    @JvmDefault
    fun readLine(prompt: String? = settings.prompt()): String? = reader.readLine(prompt)
}

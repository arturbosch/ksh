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
}

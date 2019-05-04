package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.WithPriority
import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
interface Context : WithPriority {

    val container: Injektor
    val settings: ShellSettings
    val reader: LineReader
    val terminal: Terminal
    val resolver: Resolver

    fun commands(): List<ShellClass>
}

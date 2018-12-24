package io.gitlab.arturbosch.ksh.api

import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
interface Shell {
    val terminal: Terminal
    val lineReader: LineReader
}

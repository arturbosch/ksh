package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.single
import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
abstract class Context : WithPriority {

    open var settings: ShellSettings by single()
    open var reader: LineReader by single()
    open var terminal: Terminal by single()
    open var resolver: Resolver by single()

    abstract fun commands(): List<ShellClass>
}

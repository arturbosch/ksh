package io.gitlab.arturbosch.ksh.api

import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
abstract class Context : WithPriority {

    open var settings: ShellSettings by Delegates.notNull()
    open var reader: LineReader by Delegates.notNull()
    open var terminal: Terminal by Delegates.notNull()
    open var resolver: Resolver by Delegates.notNull()

    abstract fun commands(): List<ShellClass>
}

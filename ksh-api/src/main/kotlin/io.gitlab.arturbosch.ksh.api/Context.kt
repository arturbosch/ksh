package io.gitlab.arturbosch.ksh.api

import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
abstract class Context : WithPriority {

    var settings: ShellSettings by Delegates.notNull()
    var reader: LineReader by Delegates.notNull()
    var terminal: Terminal by Delegates.notNull()
    var resolver: Resolver by Delegates.notNull()

    abstract fun commands(): List<ShellClass>
}

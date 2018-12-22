package io.gitlab.arturbosch.ksh

import org.jline.terminal.Terminal
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
object Debugging {
    var isDebug = false
    var terminal: Terminal by Delegates.notNull()

    fun log(msg: () -> Any?) {
        if (isDebug) {
            terminal.writeln(msg()?.toString())
        }
    }

    fun log(msg: Any?) {
        if (isDebug) {
            terminal.writeln(msg?.toString())
        }
    }
}

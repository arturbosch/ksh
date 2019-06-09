package io.gitlab.arturbosch.ksh

import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
class Debugging(
    var isDebug: Boolean,
    var terminal: Terminal
) {

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

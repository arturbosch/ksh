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
            writeln(msg())
        }
    }

    fun log(msg: Any?) {
        if (isDebug) {
            writeln(msg)
        }
    }

    private fun writeln(msg: Any?) = terminal.writer().write("${msg?.toString()}\n")
}

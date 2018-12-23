package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.LastExceptionState
import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.writeln
import org.jline.terminal.Terminal
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Stacktrace : ShellClass {

    private var terminal: Terminal by Delegates.notNull()

    override fun init(context: KShellContext) {
        terminal = context.terminal
    }

    @ShellMethod(help = "Prints the last error.")
    fun main() {
        terminal.writeln(LastExceptionState.error)
    }
}

package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Stacktrace : ShellClass {

    private var context: Context by Delegates.notNull()

    override fun init(context: Context) {
        this.context = context
    }

    @ShellMethod(help = "Prints the last error.")
    fun main() {
        context.writeln(context.lastExceptionState)
    }
}

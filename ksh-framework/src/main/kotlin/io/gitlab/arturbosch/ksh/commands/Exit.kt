package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import org.jline.reader.EndOfFileException

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Exit : ShellClass {

    @ShellMethod(help = "Exits the shell.")
    fun main() {
        throw EndOfFileException("")
    }
}

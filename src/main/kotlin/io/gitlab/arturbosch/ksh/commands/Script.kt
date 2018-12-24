package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.ksh.interpret
import io.gitlab.arturbosch.kutils.asPath
import io.gitlab.arturbosch.kutils.consume
import io.gitlab.arturbosch.kutils.process
import io.gitlab.arturbosch.kutils.readLines
import io.gitlab.arturbosch.kutils.single
import java.io.File
import java.io.IOException

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Script : ShellClass {

    override val commandId: String = "!"

    private val sep = java.lang.System.lineSeparator()
    private var context: Context by single()

    override fun init(context: Context) {
        this.context = context
    }

    @ShellMethod(help = "Allows to executes arbitrary shell commands.")
    fun main(
        @ShellOption(["", "--command"]) command: String,
        @ShellOption(["-wd", "--working-dir"]) workDir: File?
    ): String = try {
        val (out, err, status) = process(
                command.split(" "), workDir ?: File("."))
                .consume()
        (if (status == 0) out else err).joinToString(sep)
    } catch (ioe: IOException) {
        throw IllegalArgumentException("Error executing script 'command'.", ioe)
    }

    @ShellMethod(help = "Allows to execute ksh commands provided by a file.")
    fun file(
        @ShellOption([""], help = "Path to a ksh script") scriptName: String
    ) = try {
        context.interpret(scriptName.asPath().readLines())
        context.terminal.flush()
    } catch (ioe: IOException) {
        throw IllegalArgumentException("Error executing script 'command'.", ioe)
    }
}

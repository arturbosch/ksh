package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.kutils.consume
import io.gitlab.arturbosch.kutils.process
import java.io.File

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Script : ShellClass {

	override val commandId: String = "!"

	private val sep = java.lang.System.lineSeparator()

	@ShellMethod(help = "Allows to executes arbitrary shell commands.")
	fun main(
			@ShellOption(["", "--command"]) command: String,
			@ShellOption(["-wd", "--working-dir"]) workDir: File?
	): String {
		process(command.split(" "), workDir ?: File("."))
				.consume()
				.onSuccess { return it.joinToString(sep) }
				.onError { return it.joinToString(sep) }
		return "Command exited unexpected."
	}
}

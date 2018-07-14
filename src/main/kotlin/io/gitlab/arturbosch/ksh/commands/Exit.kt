package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import org.jline.reader.EndOfFileException

/**
 * @author Artur Bosch
 */
class Exit : ShellClass {

	@ShellMethod(help = "Exits the shell.")
	fun main() {
		throw EndOfFileException("")
	}
}

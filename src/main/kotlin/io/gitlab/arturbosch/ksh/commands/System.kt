package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class System : ShellClass {

	override val help: String = "Provides different system utils."

	@ShellMethod(help = "Prints the current date.")
	fun date(): String = LocalDate.now().toString()

	@ShellMethod(help = "Prints the current time.")
	fun time(): String = LocalDateTime.now().toString()
}

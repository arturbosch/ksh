package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.CommandProvider
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */
@ShellClass
class system : CommandProvider {

	@ShellMethod
	fun date(): String = LocalDate.now().toString()

	@ShellMethod
	fun time(): String = LocalDateTime.now().toString()
}
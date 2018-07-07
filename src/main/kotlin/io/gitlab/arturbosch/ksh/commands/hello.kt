package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption

/**
 * @author Artur Bosch
 */
class hello : ShellClass {

	@ShellMethod
	fun main(@ShellOption(["--name", "-n"]) name: String?): String = "Hello ${name ?: "World"}!"
}

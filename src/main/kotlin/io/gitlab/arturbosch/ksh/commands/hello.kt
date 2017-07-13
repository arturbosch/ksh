package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod

/**
 * @author Artur Bosch
 */
class hello : ShellClass {

	@ShellMethod
	fun main(): String = "Hello World!"
}

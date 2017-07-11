package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.CommandProvider
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
@ShellClass
class hello : CommandProvider {

	@ShellMethod
	fun main(): String = "Hello World!"
}

fun loadCommands() = ServiceLoader.load(CommandProvider::class.java).toList()
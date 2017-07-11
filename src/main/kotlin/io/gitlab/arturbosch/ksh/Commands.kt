package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.CommandProvider
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import org.kohsuke.MetaInfServices
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */

@ShellClass
@MetaInfServices
class hello : CommandProvider {

	@ShellMethod
	fun main(): String = "Hello World!"
}

fun loadCommands() = ServiceLoader.load(CommandProvider::class.java).toList()
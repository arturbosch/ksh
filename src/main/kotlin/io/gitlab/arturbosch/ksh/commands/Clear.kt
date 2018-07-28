package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.KShellContext
import org.jline.terminal.Terminal
import org.jline.utils.InfoCmp
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Clear : ShellClass {

	private var terminal: Terminal by Delegates.notNull()

	override fun init(context: KShellContext) {
		terminal = context.terminal
	}

	@ShellMethod(help = "Clears the terminal.")
	fun main() {
		terminal.puts(InfoCmp.Capability.clear_screen)
	}
}

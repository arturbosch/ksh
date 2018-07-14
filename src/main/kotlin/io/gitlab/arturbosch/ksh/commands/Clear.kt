package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.context.KShellContext
import org.jline.terminal.Terminal
import org.jline.utils.InfoCmp

/**
 * @author Artur Bosch
 */
class Clear : ShellClass {

	private var terminal: Terminal? = null

	override fun init(context: KShellContext) {
		terminal = context.terminal
	}

	@ShellMethod
	fun main() {
		terminal?.puts(InfoCmp.Capability.clear_screen)
				?: throw IllegalStateException("no terminal instance")
	}
}

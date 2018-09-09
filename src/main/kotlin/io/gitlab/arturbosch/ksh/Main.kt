package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.api.ShellBuilder

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) = bootstrap(args)

@Suppress("UNUSED_PARAMETER")
fun bootstrap(args: Array<String>) {
	val shellBuilder = loadShellBuilder()
	val context = shellBuilder.initializeShellContext()
	Bootstrap(context).start()
}

internal fun ShellBuilder.initializeShellContext(): KShellContext {
	val prompt = createPrompt()
	Debugging.isDebug = prompt.debug
	val terminal = createTerminal()
	Debugging.terminal = terminal
	val lineReader = createLineReader(prompt, terminal)

	val shellContext = loadShellContext().apply {
		this.terminal = terminal
		this.prompt = prompt
		this.reader = lineReader
	}

	val commands = shellContext.commands()
	CommandVerifier(commands)
	commands.forEach { it.init(shellContext) }

	val resolver = createResolver().init(commands)
	shellContext.resolver = resolver
	return shellContext
}

package io.gitlab.arturbosch.ksh

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	val context = loadShellContext() ?: throw IllegalStateException("No shell context found!")

	context.apply {
		val prompt = createPrompt()
		Debugging.isDebug = prompt.debug
		val terminal = createTerminal()
		Debugging.terminal = terminal
		val lineReader = createLineReader(prompt, terminal)

		val commands = loadCommands()
		Debugging.log { commands }

		val kShellContext = DefaultKShellContext(prompt, lineReader, terminal)
		commands.forEach { it.init(kShellContext) }

		val resolver = createResolver().init(commands)
		val kShell = KShell(prompt, lineReader, resolver)
		Bootstrap(kShell).start()
	}
}

package io.gitlab.arturbosch.ksh

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	val context = loadShellContext() ?: throw IllegalStateException("No shell context found!")

	context.apply {
		val prompt = createPrompt()
		val terminal = createTerminal()
		val lineReader = createLineReader(prompt, terminal)

		val commands = loadCommands()
		val kShellContext = DefaultKShellContext(prompt, lineReader, terminal)
		commands.forEach { it.init(kShellContext) }

		val resolver = createResolver().init(commands)
		val kShell = KShell(prompt, lineReader, resolver)
		Bootstrap(kShell).start()
	}
}
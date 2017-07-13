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
		val resolver = createResolver()

		val commands = loadCommands()
		resolver.init(commands)

		Bootstrap(prompt, lineReader, resolver)
				.start()
	}
}
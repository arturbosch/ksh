package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.ShellClass

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) = bootstrap(args)

@Suppress("UNUSED_PARAMETER")
fun bootstrap(args: Array<String>) {
	val context = loadShellBuilder() ?: throw IllegalStateException("No shell context found!")

	context.apply {

		val prompt = createPrompt()
		Debugging.isDebug = prompt.debug
		val terminal = createTerminal()
		Debugging.terminal = terminal
		val lineReader = createLineReader(prompt, terminal)

		val kShellContext = loadShellContext().apply {
			this.terminal = terminal
			this.prompt = prompt
			this.reader = lineReader
		}

		val commands = kShellContext.commands()
		CommandVerifier(commands)
		commands.forEach { it.init(kShellContext) }
		val resolver = createResolver().init(commands)
		val kShell = KShell(prompt, lineReader, resolver)
		Bootstrap(kShell).start()
	}
}

class CommandVerifier(val commands: List<ShellClass>) {

	init {
		Debugging.log { commands }

		val distinctIds = commands.mapTo(HashSet()) { it.commandId }
		if (commands.size != distinctIds.size) {
			val doubleIds = mutableSetOf<String>()
			for (id in commands.map { it.commandId }) {
				if (id in distinctIds) {
					distinctIds.remove(id)
				} else {
					doubleIds.add(id)
				}
			}
			throw IllegalStateException("Must not contain duplicated command ids: '$doubleIds'")
		}
	}
}

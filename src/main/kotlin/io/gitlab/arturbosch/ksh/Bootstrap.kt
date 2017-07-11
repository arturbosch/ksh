package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.commands.loadCommands
import io.gitlab.arturbosch.ksh.resolvers.DefaultResolver
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val prompt = loadPrompt() ?: throw IllegalStateException("No prompt provider found")
	val reader = reader(prompt)
	val evaluator = DefaultResolver(loadCommands())
	val ksh = Ksh(evaluator)
	Bootstrap(prompt, reader, ksh).start()
}

class Bootstrap(private val prompt: Prompt,
				private val reader: LineReader,
				private val ksh: Ksh) {

	fun start() {
		while (true) {
			try {
				val line = reader.readLine(prompt.message)
				ksh.interpret(line)
			} catch (e: ShellException) {
				e.message?.let { println(e.message) }
				e.cause?.let { println(e.cause) }
			} catch (e: UserInterruptException) {
				// Ignore
			} catch (e: EndOfFileException) {
				return
			}
		}
	}

}

private fun reader(prompt: Prompt): LineReader {
	return LineReaderBuilder.builder()
			.appName(prompt.message)
			.terminal(TerminalBuilder.terminal())
			.build()
}

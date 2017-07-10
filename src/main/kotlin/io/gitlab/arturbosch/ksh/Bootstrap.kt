package io.gitlab.arturbosch.ksh

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
	Bootstrap(prompt, reader, evaluator).start()
}

class Bootstrap(private val prompt: Prompt,
				private val reader: LineReader,
				private val resolver: Resolver) {

	fun start() {
		while (true) {
			try {
				val line = reader.readLine(prompt.message)
				resolver.evaluate(line)
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

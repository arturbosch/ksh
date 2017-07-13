package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.Resolver
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.UserInterruptException

/**
 * @author Artur Bosch
 */
class Bootstrap(private val prompt: Prompt,
				private val reader: LineReader,
				private val resolver: Resolver) {

	fun start() {
		while (true) {
			try {
				val line = reader.readLine(prompt.message)
				if (!line.isNullOrBlank()) interpret(line)
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

	fun interpret(line: String) {
		val methodTarget = resolver.evaluate(line)
		val result = methodTarget.invoke()
		println(result)
	}

}

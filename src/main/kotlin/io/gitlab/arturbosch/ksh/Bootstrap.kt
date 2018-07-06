package io.gitlab.arturbosch.ksh

import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException

/**
 * @author Artur Bosch
 */
class Bootstrap(private val kShell: KShell) {

	fun start() {
		while (true) {
			try {
				val line = kShell.readLine(kShell.message)
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

	private fun interpret(line: String) {
		val methodTarget = kShell.evaluate(line)
		val result = methodTarget.invoke()
		result?.let { println(result) }
	}
}

package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.KShellContext
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException

/**
 * @author Artur Bosch
 */
class Bootstrap(private val context: KShellContext) {

	fun start() {
		while (true) {
			try {
				context.readEvaluatePrint()
			} catch (e: UserInterruptException) {
				// Ignore
			} catch (e: EndOfFileException) {
				return // Exit repl
			} catch (e: RuntimeException) {
				context.writeln(e.toString())
				LastExceptionState.error = e
			}
		}
	}
}

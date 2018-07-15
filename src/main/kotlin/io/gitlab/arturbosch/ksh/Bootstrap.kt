package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.defaults.JLineInput
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import java.lang.reflect.InvocationTargetException

/**
 * @author Artur Bosch
 */
class Bootstrap(private val kShell: KShell) {

	fun start() {
		while (true) {
			try {
				val line = kShell.readLine(kShell.message())
				if (!line.isNullOrBlank()) {
					try { // reflection protection
						JLineInput(kShell.parsedLine).interpret()
					} catch (e: InvocationTargetException) {
						throw e.targetException
					}
				}
			} catch (e: ShellException) {
				kShell.writeln(e.message)
			} catch (e: UserInterruptException) {
				// Ignore
			} catch (e: EndOfFileException) {
				return
			} catch (e: RuntimeException) {
				kShell.writeln(e.toString())
				LastExceptionState.error = e
			}
		}
	}

	private fun InputLine.interpret() {
		val methodTarget = kShell.evaluate(this)
		val result = methodTarget.invoke()
		result?.let { kShell.writeln(result.toString()) }
	}
}

package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ExitShell
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import java.nio.file.Path
import kotlin.system.exitProcess

/**
 * @author Artur Bosch
 */
class Bootstrap(val context: Context) {

    fun runLooping() {
        while (true) {
            if (executeCatching { context.readEvaluatePrint() }) {
                return
            }
        }
    }

    fun runOnce(line: String) {
        executeCatching { context.interpret(line) }
    }

    fun runFile(path: Path) {
        context.interpret(path)
    }

    private fun executeCatching(action: () -> Unit): Boolean {
        try {
            action()
        } catch (e: UserInterruptException) {
            // Ignore
        } catch (e: EndOfFileException) {
            return true // Exit repl
        } catch (e: ExitShell) {
            exitProcess(e.exitCode)
        } catch (e: RuntimeException) {
            context.writeln(e.message)
            LastExceptionState.error = e
        }
        return false
    }
}

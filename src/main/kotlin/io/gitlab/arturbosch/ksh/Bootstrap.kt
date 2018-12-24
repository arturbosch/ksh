package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ExitShell
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import kotlin.system.exitProcess

/**
 * @author Artur Bosch
 */
class Bootstrap(private val context: Context) {

    fun start() {
        while (true) {
            try {
                context.readEvaluatePrint()
            } catch (e: UserInterruptException) {
                // Ignore
            } catch (e: EndOfFileException) {
                return // Exit repl
            } catch (e: ExitShell) {
                exitProcess(e.exitCode)
            } catch (e: RuntimeException) {
                context.writeln(e.message)
                LastExceptionState.error = e
            }
        }
    }
}

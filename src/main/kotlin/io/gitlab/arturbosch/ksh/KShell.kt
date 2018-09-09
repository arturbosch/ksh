package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.CallTarget
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.defaults.JLineInput
import org.jline.reader.ParsedLine
import org.jline.reader.Parser
import org.jline.reader.impl.DefaultParser
import java.lang.reflect.InvocationTargetException

/**
 * @author Artur Bosch
 */
class DefaultKShellContext : KShellContext() {

	private val _commands: List<ShellClass> by lazy { loadCommands() }

	override val priority: Int = Int.MIN_VALUE
	override fun commands(): List<ShellClass> = _commands
}

fun KShellContext.writeln(msg: String?) = terminal.writeln(msg)
fun KShellContext.resolve(line: InputLine) = resolver.evaluate(line)
fun KShellContext.readLine(): String? = reader.readLine(prompt.message())
fun KShellContext.parsedLine(): ParsedLine = reader.parsedLine
	?: throw IllegalStateException("Do not get a 'ParsedLine' before reading a line first.")

fun KShellContext.call(target: CallTarget) {
	val result = target.invoke()
	if (result != null && result != Unit) {
		writeln(result.toString())
	}
}

fun KShellContext.interpret(line: String, parser: Parser = DefaultParser()) {
	val parsedLine = parser.parse(line, 0, Parser.ParseContext.ACCEPT_LINE)
	val input = JLineInput(parsedLine)
	call(resolve(input))
}

fun KShellContext.interpret(lines: List<String>) {
	val parser = DefaultParser()
	for (line in lines) {
		interpret(line, parser)
	}
}

fun KShellContext.readEvaluatePrint() {
	try {
		val line = readLine()
		if (!line.isNullOrBlank()) {
			try { // reflection protection
				val input = JLineInput(parsedLine())
				val callTarget = resolve(input)
				call(callTarget)
			} catch (e: InvocationTargetException) {
				throw e.targetException
			}
		}
	} catch (e: ShellException) {
		writeln(e.message)
	}
}

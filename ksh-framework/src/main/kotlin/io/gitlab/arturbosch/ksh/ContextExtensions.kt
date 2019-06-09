package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.CallTarget
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.ShellException
import io.gitlab.arturbosch.ksh.defaults.JLineInput
import io.gitlab.arturbosch.kutils.streamLines
import org.jline.reader.ParsedLine
import org.jline.reader.Parser
import org.jline.reader.impl.DefaultParser
import java.lang.reflect.InvocationTargetException
import java.nio.file.Path

fun Context.writeln(msg: String?) = terminal.writeln(msg)
fun Context.resolve(line: InputLine) = resolvers.asSequence()
    .map { it.evaluate(line) }
    .firstOrNull()
    ?: throw ShellException("No resolver could resolve '$line'")

fun Context.readLine(prompt: String? = settings.prompt()): String? = reader.readLine(prompt)
fun Context.parsedLine(): ParsedLine = reader.parsedLine
    ?: throw IllegalStateException("Do not get a 'ParsedLine' before reading a line first.")

fun Context.call(target: CallTarget) {
    val result = target.invoke()
    if (result != null && result != Unit) {
        writeln(result.toString())
        terminal.flush()
    }
}

fun Context.interpret(line: String, parser: Parser = DefaultParser()) {
    val parsedLine = parser.parse(line, 0, Parser.ParseContext.ACCEPT_LINE)
    val input = JLineInput(parsedLine)
    call(resolve(input))
}

fun Context.interpret(path: Path, parser: Parser = DefaultParser()) {
    path.streamLines().forEach { interpret(it, parser) }
}

fun Context.readEvaluatePrint() {
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

package io.gitlab.arturbosch.ksh.test

import io.gitlab.arturbosch.ksh.NoopContainer
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.bootstrap
import io.gitlab.arturbosch.ksh.defaults.DefaultShellBuilder
import io.gitlab.arturbosch.ksh.defaults.JLineInput
import io.gitlab.arturbosch.ksh.resolve
import io.gitlab.arturbosch.kutils.resourceAsStream
import org.jline.reader.Parser
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.Terminal
import org.jline.terminal.impl.DumbTerminal
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * @author Artur Bosch
 */
class TestShellBuilder(
    private val `in`: InputStream = System.`in`,
    private val out: OutputStream = System.out
) : DefaultShellBuilder() {

    override fun createTerminal(): Terminal = DumbTerminal(`in`, out)
}

fun testContext(builder: DefaultShellBuilder = TestShellBuilder()): Context =
    bootstrap(NoopContainer(), builder).context

inline fun <reified T : ShellClass> Context.get() =
    commands().find { it is T } as? T
        ?: throw IllegalStateException("No such command '${T::class.java}'.")

fun resourceAsTmpFile(resourceName: String): File {
    val tempFile = File.createTempFile("ksh", System.currentTimeMillis().toString())
    resourceAsStream(resourceName).copyTo(tempFile.outputStream())
    return tempFile
}

fun Context.test(line: String): Any? {
    val parsedLine = DefaultParser().parse(line, 0, Parser.ParseContext.ACCEPT_LINE)
    val input = JLineInput(parsedLine)
    return resolve(input).invoke()
}

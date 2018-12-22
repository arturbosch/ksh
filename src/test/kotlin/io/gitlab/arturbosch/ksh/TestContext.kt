package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.defaults.DefaultShellBuilder
import io.gitlab.arturbosch.kutils.resourceAsStream
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

fun testContext(builder: DefaultShellBuilder = TestShellBuilder()) =
        builder.initializeShellContext()

inline fun <reified T> KShellContext.get() = commands().find { it is T } as? T
    ?: throw IllegalStateException("No such command '${T::class.java}'.")

fun resourceAsTmpFile(resourceName: String): File {
    val tempFile = File.createTempFile("ksh", System.currentTimeMillis().toString())
    resourceAsStream(resourceName)
            .copyTo(tempFile.outputStream())
    return tempFile
}

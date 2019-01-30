package io.gitlab.arturbosch.ksh.commands

import assertk.assertions.isEqualTo
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import io.gitlab.arturbosch.ksh.test.TestShellBuilder
import io.gitlab.arturbosch.ksh.test.get
import io.gitlab.arturbosch.ksh.test.resourceAsTmpFile
import io.gitlab.arturbosch.ksh.test.testContext
import org.junit.Test

/**
 * @author Artur Bosch
 */
internal class ScriptTest {

    @Test
    fun `allows to execute arbitrary scripts with ksh commands in it`() {
        val stream = ByteOutputStream()
        val context = testContext(TestShellBuilder(out = stream))
        val script = context.get<Script>()
        val tmpFile = resourceAsTmpFile("hello_script")

        script.file(tmpFile.toString())

        val actual = stream.toString()
        val expected = """
			Hello World!
			Hello Artur!
			Hello World!

		""".trimIndent()

        assertk.assert(actual).isEqualTo(expected)
    }
}

package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.test.test
import io.gitlab.arturbosch.ksh.test.testContext
import org.junit.Test
import kotlin.test.assertEquals

class HelloTest {

    private val context = testContext()

    @Test
    fun `hello world is printed`() {
        assertEquals("Hello World", context.test("hello"))
    }

    @Test
    fun `hello name is printed`() {
        assertEquals("Hello Artur", context.test("hello --name Artur"))
    }
}

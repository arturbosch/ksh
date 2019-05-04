package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.test.TestResolver
import org.junit.Test
import kotlin.test.assertEquals

class HelloTest {

    private val resolver = TestResolver().init(listOf(Hello()))

    @Test
    fun `hello world is printed`() {
        assertEquals("Hello World", resolver.evaluate("hello"))
    }

    @Test
    fun `hello name is printed`() {
        assertEquals("Hello Artur", resolver.evaluate("hello --name Artur"))
    }
}

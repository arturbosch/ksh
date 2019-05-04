package io.gitlab.arturbosch.ksh.defaults

import assertk.assertions.isEqualTo
import assertk.assertions.isIn
import assertk.assertions.isInstanceOf
import io.gitlab.arturbosch.ksh.Conversions
import io.gitlab.arturbosch.ksh.DoubleMain
import io.gitlab.arturbosch.ksh.Hello
import io.gitlab.arturbosch.ksh.test.testResolver
import org.junit.Test
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class DefaultResolverTest {

    private val resolver = testResolver(Hello(), Conversions(), DoubleMain())

    @Test
    fun resolveMainWithOneOptionIdLessParameter() {
        val target = resolver.evaluate("hello Artur")
        assertk.assert(target).isEqualTo("Hello Artur!")
    }

    @Test
    fun resolveMainWithOneParameter() {
        val target = resolver.evaluate("hello --name Artur")
        assertk.assert(target).isEqualTo("Hello Artur!")
    }

    @Test
    fun resolveMainWithOneDefaultParameter() {
        val target = resolver.evaluate("hello")
        assertk.assert(target).isEqualTo("Hello World!")
    }

    @Test
    fun resolveMethodWithOneParameter() {
        val target = resolver.evaluate("hello say --name Artur")
        assertk.assert(target).isEqualTo("Hello Artur!")
    }

    @Test
    fun resolveMethodWithOneDefaultParameter() {
        val target = resolver.evaluate("hello say")
        assertk.assert(target).isEqualTo("Hello OMG!")
    }

    @Test
    fun resolveMethodWithTwoParametersAndDefaults() {
        val target = resolver.evaluate("hello count")
        assertk.assert(target).isEqualTo("Hello OMGOMGOMG")
    }

    @Test
    fun resolveMethodWithTwoParametersOneDefault() {
        val target = resolver.evaluate("hello count --name Artur")
        assertk.assert(target).isEqualTo("Hello ArturArturArtur")
    }

    @Test
    fun resolveMethodWithTwoParametersNoDefaults() {
        val target = resolver.evaluate("hello count --name Artur --times 1")
        assertk.assert(target).isEqualTo("Hello Artur")
    }

    @Test
    fun resolveClassWithCustomIdAndMethodWithAllConversionsAndRandomParameters() {
        val command = "conversions -file /home/test -i 5 -b -f 2.0f -d 5.0 -s bla"
        val target = resolver.evaluate(command)
        assertk.assert(target).isEqualTo("5 2.0 true 5.0 bla /home/test")
    }

    @Test
    fun resolveTwoUnnamedParameters() {
        val target = resolver.evaluate("hello invalid Artur 25")
        assertk.assert(target).isEqualTo("Hello Artur - 25")
    }

    @Test
    fun resolvesPathParameters() {
        val target = resolver.evaluate("conversions path this/is/a/path")
        assertk.assert(target!!).isInstanceOf(Path::class)
    }

    @Test
    fun resolvesToDefaultMainOnEmptyInput() {
        assertk.assert(resolver.evaluate("double")).isEqualTo("first")
    }

    @Test
    fun resolvedToAnyIfMainIsSpecifiedMoreThanOneTime() {
        assertk.assert(resolver.evaluate("double main")).isIn("first", "second")
    }
}

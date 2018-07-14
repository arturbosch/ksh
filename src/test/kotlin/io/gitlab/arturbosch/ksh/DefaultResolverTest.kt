package io.gitlab.arturbosch.ksh

import assertk.assertions.isEqualTo
import org.junit.Test

/**
 * @author Artur Bosch
 */
class DefaultResolverTest {

	private val resolver = TestResolver()
			.init(listOf(Hello(), Conversions())) as TestResolver

	@Test
	fun resolveMainWithOneOptionIdLessParameter() {
		val target = resolver.evaluate("hello Artur")
		assertk.assert(target.invoke()).isEqualTo("Hello Artur!")
	}

	@Test
	fun resolveMainWithOneParameter() {
		val target = resolver.evaluate("hello --name Artur")
		assertk.assert(target.invoke()).isEqualTo("Hello Artur!")
	}

	@Test
	fun resolveMainWithOneDefaultParameter() {
		val target = resolver.evaluate("hello")
		assertk.assert(target.invoke()).isEqualTo("Hello World!")
	}

	@Test
	fun resolveMethodWithOneParameter() {
		val target = resolver.evaluate("hello say --name Artur")
		assertk.assert(target.invoke()).isEqualTo("Hello Artur!")
	}

	@Test
	fun resolveMethodWithOneDefaultParameter() {
		val target = resolver.evaluate("hello say")
		assertk.assert(target.invoke()).isEqualTo("Hello OMG!")
	}

	@Test
	fun resolveMethodWithTwoParametersAndDefaults() {
		val target = resolver.evaluate("hello count")
		assertk.assert(target.invoke()).isEqualTo("Hello OMGOMGOMG")
	}

	@Test
	fun resolveMethodWithTwoParametersOneDefault() {
		val target = resolver.evaluate("hello count --name Artur")
		assertk.assert(target.invoke()).isEqualTo("Hello ArturArturArtur")
	}

	@Test
	fun resolveMethodWithTwoParametersNoDefaults() {
		val target = resolver.evaluate("hello count --name Artur --times 1")
		assertk.assert(target.invoke()).isEqualTo("Hello Artur")
	}

	@Test
	fun resolveClassWithCustomIdAndMethodWithAllConversionsAndRandomParameters() {
		val command = "conversions -file /home/test -i 5 -b -f 2.0f -d 5.0 -s bla"
		val target = resolver.evaluate(command)
		assertk.assert(target.invoke()).isEqualTo("5 2.0 true 5.0 bla /home/test")
	}

	@Test
	fun resolveTwoUnnamedParameters() {
		val target = resolver.evaluate("hello invalid Artur 25")
		assertk.assert(target.invoke()).isEqualTo("Hello Artur - 25")
	}
}

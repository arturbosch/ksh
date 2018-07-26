package io.gitlab.arturbosch.ksh

import assertk.assertions.isEqualTo
import org.junit.Test

/**
 * @author Artur Bosch
 */
class FixedResolverBugsTest {

	private val resolver = TestResolver()
			.init(listOf(Gradle())) as TestResolver

	@Test
	fun `should skip non flag words after arity is resolved`() {
		val target = resolver.evaluate("gradle module testy -wd /bla/bla -g a.b.c")
		assertk.assert(target.invoke()).isEqualTo("")
	}
}

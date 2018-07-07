package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.commands.hello
import io.gitlab.arturbosch.ksh.defaults.DefaultResolver
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Artur Bosch
 */
class DefaultResolverTest {

	@Test
	fun resolveMainMethod() {
		val resolver = DefaultResolver().init(listOf(hello()))
		val methodTarget = resolver.evaluate("hello --name Artur")
		val actual = methodTarget.invoke()

		assertEquals("Hello Artur!", actual)
	}
}

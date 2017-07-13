package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.commands.hello
import io.gitlab.arturbosch.ksh.resolvers.DefaultResolver
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Artur Bosch
 */
class DefaultResolverTest {

	@Test
	fun resolveMainMethod() {
		val resolver = DefaultResolver(listOf(hello()))
		val methodTarget = resolver.evaluate("hello")
		val actual = methodTarget.invoke()

		assertEquals("Hello World!", actual)
	}
}
package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.resolvers.DefaultResolver
import org.junit.Test

/**
 * @author Artur Bosch
 */
class DefaultResolverTest {

	@Test
	fun resolveMainMethod() {
		val resolver = DefaultResolver(listOf(hello()))
		val methodTarget = resolver.evaluate("hello")

		println(methodTarget)
	}
}
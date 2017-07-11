package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.resolvers.Resolver

/**
 * @author Artur Bosch
 */
class Ksh(val resolver: Resolver) {

	fun interpret(line: String) {
		val methodTarget = resolver.evaluate(line)
		val result = methodTarget?.invoke() ?: throw ShellException("No matching command found for $line")
		println(result)
	}
}
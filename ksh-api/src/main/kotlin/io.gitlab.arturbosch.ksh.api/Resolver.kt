package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
abstract class Resolver(val commands: List<ShellClass>) : WithPriority {

	abstract fun evaluate(input: String): MethodTarget
}

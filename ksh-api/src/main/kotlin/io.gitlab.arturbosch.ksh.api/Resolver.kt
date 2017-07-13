package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
abstract class Resolver : WithPriority {

	abstract fun init(commands: List<ShellClass>)
	abstract fun evaluate(input: String): MethodTarget
}

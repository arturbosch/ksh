package io.gitlab.arturbosch.ksh.resolvers

import io.gitlab.arturbosch.ksh.WithPriority
import io.gitlab.arturbosch.ksh.api.ShellClass

/**
 * @author Artur Bosch
 */
abstract class Resolver(val commands: List<ShellClass>) : WithPriority {
	abstract fun evaluate(input: String): MethodTarget
}

val SPACE = " "
val OPTION_START = "--"
val MAIN_METHOD_NAME = "main"

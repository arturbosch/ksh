package io.gitlab.arturbosch.ksh.resolvers

import io.gitlab.arturbosch.ksh.WithPriority
import io.gitlab.arturbosch.ksh.api.CommandProvider

/**
 * @author Artur Bosch
 */
abstract class Resolver(val commands: List<CommandProvider>) : WithPriority {
	abstract fun evaluate(input: String?): MethodTarget?
}
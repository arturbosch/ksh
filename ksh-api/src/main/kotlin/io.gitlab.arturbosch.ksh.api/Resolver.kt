package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority

/**
 * @author Artur Bosch
 */
interface Resolver : WithPriority {

    fun init(commands: List<ShellClass>): Resolver
    fun evaluate(input: InputLine): CallTarget
}

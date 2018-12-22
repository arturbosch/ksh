package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface Resolver : WithPriority {

    fun init(commands: List<ShellClass>): Resolver
    fun evaluate(input: InputLine): CallTarget
}

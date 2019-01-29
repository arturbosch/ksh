package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.loadedCommands

/**
 * @author Artur Bosch
 */
class DefaultContext : Context() {

    override val priority: Int = Int.MIN_VALUE
    override fun commands(): List<ShellClass> = loadedCommands
}

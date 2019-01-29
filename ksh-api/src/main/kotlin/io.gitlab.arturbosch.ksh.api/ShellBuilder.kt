package io.gitlab.arturbosch.ksh.api

import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
interface ShellBuilder : WithPriority {

    fun createTerminal(): Terminal
    fun createShell(settings: ShellSettings): Shell
}

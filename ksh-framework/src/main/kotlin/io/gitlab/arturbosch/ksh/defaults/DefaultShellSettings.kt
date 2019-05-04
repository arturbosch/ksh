package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.ShellSettings

/**
 * @author Artur Bosch
 */
class DefaultShellSettings : ShellSettings {

    override val applicationName: String = "ksh"
    override val historyFile: String = "./ksh-history"
    override val priority: Int = -1
    override fun prompt(): String = "ksh> "
}

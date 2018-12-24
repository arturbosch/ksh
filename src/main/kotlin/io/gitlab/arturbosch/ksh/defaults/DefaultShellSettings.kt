package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.loadedCommands
import org.jline.reader.Completer

/**
 * @author Artur Bosch
 */
class DefaultShellSettings : ShellSettings {

    override val applicationName: String = "ksh"
    override val historyFile: String = "./ksh-history"
    override val priority: Int = -1
    override fun prompt(): String = "ksh> "
    override fun customCompleter(): Completer? = DefaultCompleter(loadedCommands)
}

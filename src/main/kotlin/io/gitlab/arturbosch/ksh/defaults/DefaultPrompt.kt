package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Prompt

/**
 * @author Artur Bosch
 */
class DefaultPrompt : Prompt {

    override val applicationName: String = "ksh"
    override val historyFile: String = "./ksh-history"
    override val priority: Int = -1
    override fun message(): String = "ksh> "
}

package io.gitlab.arturbosch.ksh.api

import org.jline.reader.Completer

/**
 * @author Artur Bosch
 */
interface ShellSettings : WithPriority {

    val debug: Boolean get() = false
    val applicationName: String
    val historyFile: String
    fun prompt(): String
    fun customCompleter(): Completer? = null
}

package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority

/**
 * @author Artur Bosch
 */
interface ShellSettings : WithPriority {

    val debug: Boolean get() = false
    val applicationName: String
    val historyFile: String
    fun prompt(): String
}

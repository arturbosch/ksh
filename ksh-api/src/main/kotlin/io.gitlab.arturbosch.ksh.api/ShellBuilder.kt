package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.WithPriority

/**
 * @author Artur Bosch
 */
interface ShellBuilder : WithPriority {

    fun createShell(settings: ShellSettings, container: Injektor): Shell
}

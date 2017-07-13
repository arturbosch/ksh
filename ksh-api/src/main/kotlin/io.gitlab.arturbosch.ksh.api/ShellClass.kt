package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.ksh.api.context.KShellContext

/**
 * @author Artur Bosch
 */
interface ShellClass {

	fun init(context: KShellContext) {}
}

package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.ShellContext
import io.gitlab.arturbosch.ksh.api.Prompt

/**
 * @author Artur Bosch
 */
class DefaultShellContext : ShellContext {

	override fun createPrompt(): Prompt = DefaultPrompt()
}
package io.gitlab.arturbosch.ksh.internal

import io.gitlab.arturbosch.ksh.api.KshContext
import io.gitlab.arturbosch.ksh.api.Prompt

/**
 * @author Artur Bosch
 */
class DefaultContext : KshContext {

	override fun createPrompt(): Prompt = DefaultPrompt()
}
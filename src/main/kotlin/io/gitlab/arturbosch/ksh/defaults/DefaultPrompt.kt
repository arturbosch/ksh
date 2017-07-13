package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Prompt

/**
 * @author Artur Bosch
 */

class DefaultPrompt : Prompt {
	override var message: String = "ksh> "
	override val priority: Int = -1
}

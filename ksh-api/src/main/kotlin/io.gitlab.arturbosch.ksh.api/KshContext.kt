package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface KshContext {

	fun createPrompt(): Prompt
}
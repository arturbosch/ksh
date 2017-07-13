package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface ShellContext {

	fun createPrompt(): Prompt
}
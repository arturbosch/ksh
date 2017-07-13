package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface Prompt : WithPriority {
	var message: String
}
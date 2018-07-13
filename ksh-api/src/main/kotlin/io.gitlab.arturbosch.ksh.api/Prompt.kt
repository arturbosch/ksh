package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface Prompt : WithPriority {

	val applicationName: String

	fun message(): String
}

package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ShellMethod(
		val value: Array<String> = [],
		val help: String = "",
		val prefix: String = "--")

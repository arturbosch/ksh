package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ShellOption(val value: Array<String>,
							 val arity: Int = ShellOptions.EXTRACT_ARITY)

object ShellOptions {
	const val EXTRACT_ARITY: Int = -1
}

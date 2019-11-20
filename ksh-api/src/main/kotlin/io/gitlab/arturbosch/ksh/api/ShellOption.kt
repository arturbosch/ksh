package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ShellOption(
    val value: Array<String>,
    val arity: Int = EXTRACT_ARITY,
    val defaultValue: String = NULL_DEFAULT,
    val help: String = ""
) {

    companion object {
        const val EXTRACT_ARITY: Int = -1
        const val NULL_DEFAULT: String = "__null__"
    }
}

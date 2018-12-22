package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface ShellClass {

    val commandId: String get() = this.javaClass.simpleName.toLowerCase()
    val help: String get() = ""
    fun init(context: KShellContext) {}
}

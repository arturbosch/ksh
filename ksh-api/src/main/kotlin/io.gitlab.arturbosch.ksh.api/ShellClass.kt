package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface ShellClass {

    @JvmDefault
    val commandId: String
        get() = this.javaClass.simpleName.toLowerCase()

    @JvmDefault
    val help: String
        get() = ""

    @JvmDefault
    fun init(context: Context) {
    }
}

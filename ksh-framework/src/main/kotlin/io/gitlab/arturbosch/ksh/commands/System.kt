package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.kutils.SimpleStringNode
import io.gitlab.arturbosch.kutils.toTreeString
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class System : ShellClass {

    override val help: String = "Provides different system utils."

    @ShellMethod(help = "Prints the current date.")
    fun date(): String = LocalDate.now().toString()

    @ShellMethod(help = "Prints the current time.")
    fun time(): String = LocalDateTime.now().toString()

    @ShellMethod(help = "Lists environment variables.")
    fun env(
        @ShellOption(value = ["", "--name"], help = "Name of the environment variable to print.") name: String? = null
    ): String = if (name == null) {
        toTreeString(SimpleStringNode(
            "Environment Variables",
            java.lang.System.getenv()
                .entries
                .map { SimpleStringNode("${it.key}=${it.value}", emptyList()) }
        ))
    } else {
        java.lang.System.getenv(name)
    }
}

package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod

class Hello : ShellClass {

    @ShellMethod(help = "Prints Hello World.")
    fun main() = "Hello World"
}

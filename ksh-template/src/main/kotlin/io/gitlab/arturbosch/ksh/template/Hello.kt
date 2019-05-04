package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption

class Hello : ShellClass {

    @ShellMethod
    fun main(@ShellOption(["--name", "-n", ""]) name: String?): String = "Hello ${name ?: "World"}"
}

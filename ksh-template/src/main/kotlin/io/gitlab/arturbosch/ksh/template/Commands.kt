package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.kutils.Injektor

class CommandsProvider : ShellClassesProvider {

    override fun provide(container: Injektor): List<ShellClass> = listOf(Hello())
}

class Hello : ShellClass {

    @ShellMethod
    fun main(@ShellOption(["--name", "-n", ""]) name: String?): String = "Hello ${name ?: "World"}"
}

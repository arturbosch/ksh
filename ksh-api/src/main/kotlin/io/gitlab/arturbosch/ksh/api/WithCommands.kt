package io.gitlab.arturbosch.ksh.api

interface WithCommands {

    fun init(commands: List<ShellClass>)
}

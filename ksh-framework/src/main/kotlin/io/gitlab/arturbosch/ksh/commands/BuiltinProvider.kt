package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.kutils.Injektor

class BuiltinProvider : ShellClassesProvider {

    override fun provide(container: Injektor): List<ShellClass> =
        listOf(
            Clear(),
            Exit(),
            Help(),
            Script(),
            Stacktrace(),
            System()
        )
}

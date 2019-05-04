package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.kutils.Injektor

class TestCommandsProvider : ShellClassesProvider {

    override fun provide(container: Injektor): List<ShellClass> = listOf(
        Hello(),
        Gradle(),
        Conversions(),
        DoubleMain()
    )
}

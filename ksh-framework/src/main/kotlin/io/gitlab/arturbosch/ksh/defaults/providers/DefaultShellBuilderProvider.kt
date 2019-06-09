package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.provider.ShellBuilderProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShellBuilder
import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.withSingleton

class DefaultShellBuilderProvider : ShellBuilderProvider, WithLowPriority {

    override fun provide(container: Container): ShellBuilder {
        return container.withSingleton(DefaultShellBuilder())
    }
}

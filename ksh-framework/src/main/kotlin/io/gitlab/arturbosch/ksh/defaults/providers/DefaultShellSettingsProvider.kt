package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShellSettings
import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.withSingleton

class DefaultShellSettingsProvider : ShellSettingsProvider, WithLowPriority {

    override fun provide(container: Container): ShellSettings {
        return container.withSingleton(DefaultShellSettings())
    }
}

package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShellSettings
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.withSingleton

class DefaultShellSettingsProvider : ShellSettingsProvider {

    override fun provide(container: Injektor): ShellSettings {
        return container.withSingleton(DefaultShellSettings())
    }
}

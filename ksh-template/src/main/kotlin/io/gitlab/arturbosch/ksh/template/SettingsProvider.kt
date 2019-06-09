package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.kutils.Container
import java.nio.file.Files

class SettingsProvider : ShellSettingsProvider {

    override fun provide(container: Container): ShellSettings {
        return object : ShellSettings {
            override val applicationName: String = "app"
            override val historyFile: String = Files.createTempFile(applicationName, "history").toString()
            override fun prompt(): String = "$applicationName> "
        }
    }
}


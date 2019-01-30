package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.api.ShellSettings
import java.nio.file.Files

class AppSettings : ShellSettings {

    override val applicationName: String = "app"
    override val historyFile: String = Files.createTempFile("app", "history").toString()
    override fun prompt(): String = "app> "
}

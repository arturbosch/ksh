package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.ShellBuilder

/**
 * @author Artur Bosch
 */
fun main() = bootstrap()

fun bootstrap() {
    val context = loadShellBuilder().initializeShellContext()
    Bootstrap(context).start()
}

fun ShellBuilder.initializeShellContext(): Context {
    val settings = loadSettings()
    Debugging.isDebug = settings.debug

    val shell = createShell(settings)
    Debugging.terminal = shell.terminal

    val shellContext = loadShellContext()
    val commands = shellContext.commands()
    CommandVerifier(commands)

    shellContext.apply {
        this.terminal = shell.terminal
        this.settings = settings
        this.reader = shell.lineReader
    }

    commands.forEach { it.init(shellContext) }

    shellContext.resolver = loadResolver().init(commands)
    return shellContext
}

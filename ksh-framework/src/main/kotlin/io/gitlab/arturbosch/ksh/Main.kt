package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ContextProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellBuilderProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShell
import io.gitlab.arturbosch.ksh.defaults.DefaultShellBuilder
import io.gitlab.arturbosch.ksh.defaults.DefaultShellSettings
import io.gitlab.arturbosch.kutils.DefaultInjektor
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.addSingleton
import io.gitlab.arturbosch.kutils.get
import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
fun main() = bootstrap()

fun bootstrap() {
    bootstrap(DefaultInjektor())
}

fun ShellBuilder.initializeShellContext(): Context {
    val settings = loadSettings()
    Debugging.isDebug = settings.debug

    val shell = createShell(settings)
    Debugging.terminal = shell.terminal

    val shellContext = loadShellContext()
    val commands = shellContext.commands()
    verify(commands)

    shellContext.apply {
        this.terminal = shell.terminal
        this.settings = settings
        this.reader = shell.lineReader
    }

    commands.forEach { it.init(shellContext) }

    shellContext.resolver = loadResolver().init(commands)
    return shellContext
}

fun bootstrap(container: Injektor) {
    val settings = object : ShellSettingsProvider {
        override fun provide(container: Injektor): ShellSettings = DefaultShellSettings()
    }.provide(container)

    container.addSingleton(settings)

    val builder = object : ShellBuilderProvider {
        override fun provide(container: Injektor): ShellBuilder = DefaultShellBuilder()
    }.provide(container)

    val (term, reader) = builder.createShell(settings) as DefaultShell

    container.addSingleton(term)
    container.addSingleton(reader)

    verify(loadedCommands)
    val resolver = loadResolver().init(loadedCommands)
    container.addSingleton(resolver)

    val context = object : ContextProvider {
        override fun provide(container: Injektor): Context = object : Context() {
            override var settings: ShellSettings = container.get()
            override var reader: LineReader = container.get()
            override var terminal: Terminal = container.get()
            override var resolver: Resolver = container.get()
            override fun commands(): List<ShellClass> = loadedCommands
        }
    }.provide(container)

    container.addSingleton(context)
    loadedCommands.forEach { it.init(context) }
    Bootstrap(context).start()
}

private operator fun DefaultShell.component2(): LineReader = this.lineReader
private operator fun DefaultShell.component1(): Terminal = this.terminal

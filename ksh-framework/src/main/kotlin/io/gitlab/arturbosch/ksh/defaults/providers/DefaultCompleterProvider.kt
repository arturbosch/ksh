package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.provider.CompleterProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultCommandAndPathCompleter
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.load
import io.gitlab.arturbosch.kutils.withSingleton
import org.jline.reader.Completer

class DefaultCompleterProvider : CompleterProvider {

    override fun provide(container: Injektor): Completer {
        val commands = load<ShellClassesProvider>().flatMap { it.provide(container) }
        return container.withSingleton(DefaultCommandAndPathCompleter(commands))
    }
}

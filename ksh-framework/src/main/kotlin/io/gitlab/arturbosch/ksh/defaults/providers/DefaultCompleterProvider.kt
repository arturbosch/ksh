package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.ksh.api.Completer
import io.gitlab.arturbosch.ksh.api.provider.CompleterProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultCommandAndPathCompleter
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.withSingleton

class DefaultCompleterProvider : CompleterProvider, WithLowPriority {

    override fun provide(container: Injektor): Completer {
        return container.withSingleton(DefaultCommandAndPathCompleter())
    }
}

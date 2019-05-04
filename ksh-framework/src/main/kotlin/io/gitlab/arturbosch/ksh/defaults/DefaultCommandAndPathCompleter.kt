package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Completer
import io.gitlab.arturbosch.ksh.api.ShellClass
import org.jline.builtins.Completers
import org.jline.reader.impl.completer.AggregateCompleter

/**
 * @author Artur Bosch
 */
class DefaultCommandAndPathCompleter(
    private val defaultCompleter: Completer = DefaultCompleter()
) : AggregateCompleter(
    defaultCompleter,
    Completers.FileNameCompleter()
), Completer {

    override fun init(commands: List<ShellClass>) {
        defaultCompleter.init(commands)
    }
}

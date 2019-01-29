package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.loadedCommands
import org.jline.builtins.Completers
import org.jline.reader.impl.completer.AggregateCompleter

/**
 * @author Artur Bosch
 */
class DefaultCommandAndPathCompleter : AggregateCompleter(
        DefaultCompleter(loadedCommands),
        Completers.FileNameCompleter()
)

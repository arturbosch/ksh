package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.ShellClass
import org.jline.builtins.Completers
import org.jline.reader.impl.completer.AggregateCompleter

/**
 * @author Artur Bosch
 */
class DefaultCommandAndPathCompleter(
    commands: Collection<ShellClass>
) : AggregateCompleter(
    DefaultCompleter(commands),
    Completers.FileNameCompleter()
)

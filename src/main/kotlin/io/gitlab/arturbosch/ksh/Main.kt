package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.defaults.DefaultCompleter
import org.jline.builtins.Completers
import org.jline.reader.impl.completer.AggregateCompleter

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) = bootstrap(args)

@Suppress("UNUSED_PARAMETER")
fun bootstrap(args: Array<String>) {
    val shellBuilder = loadShellBuilder()
    val context = shellBuilder.initializeShellContext()
    Bootstrap(context).start()
}

internal fun ShellBuilder.initializeShellContext(): KShellContext {
    val prompt = createPrompt()
    Debugging.isDebug = prompt.debug

    val terminal = createTerminal()
    Debugging.terminal = terminal

    val shellContext = loadShellContext()
    val commands = shellContext.commands()
    CommandVerifier(commands)

    val lineReader = createLineReader(prompt, terminal) {
        this.completer(AggregateCompleter(
                DefaultCompleter(commands),
                Completers.FileNameCompleter()
        ))
    }

    shellContext.apply {
        this.terminal = terminal
        this.prompt = prompt
        this.reader = lineReader
    }

    commands.forEach { it.init(shellContext) }

    val resolver = createResolver().init(commands)
    shellContext.resolver = resolver
    return shellContext
}

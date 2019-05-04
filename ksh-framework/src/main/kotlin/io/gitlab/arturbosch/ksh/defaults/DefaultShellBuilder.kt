package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Shell
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.CompleterProvider
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.firstPrioritized
import io.gitlab.arturbosch.kutils.load
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.DefaultParser
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

/**
 * @author Artur Bosch
 */
open class DefaultShellBuilder : ShellBuilder {

    override val priority: Int = -1

    open fun createTerminal(): Terminal = TerminalBuilder.terminal()

    override fun createShell(settings: ShellSettings, container: Injektor): Shell {
        val history = DefaultHistory()
        val terminal = createTerminal()
        val completer = load<CompleterProvider>()
            .firstPrioritized()
            ?.provide(container)
        val reader = LineReaderBuilder.builder()
            .appName(settings.applicationName)
            .terminal(terminal)
            .history(history)
            .variable(LineReader.HISTORY_FILE, settings.historyFile)
            .parser(DefaultParser())
            .completer(completer)
            .build()
        history.attach(reader)
        return DefaultShell(terminal, reader)
    }
}

open class DefaultShell(
    override val terminal: Terminal,
    override val lineReader: LineReader
) : Shell {

    operator fun component1(): Terminal = this.terminal
    operator fun component2(): LineReader = this.lineReader
}

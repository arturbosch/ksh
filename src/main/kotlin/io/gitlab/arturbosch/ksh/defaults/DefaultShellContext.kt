package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellContext
import io.gitlab.arturbosch.ksh.loadPrompt
import io.gitlab.arturbosch.ksh.loadResolver
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

/**
 * @author Artur Bosch
 */
class DefaultShellContext : ShellContext {

	override val priority: Int = -1

	override fun createPrompt(): Prompt = loadPrompt() ?: throw IllegalStateException("No prompt provider found!")
	override fun createResolver(): Resolver = loadResolver() ?: throw IllegalStateException("No resolver found!")
	override fun createTerminal(): Terminal = TerminalBuilder.terminal()
	override fun createLineReader(prompt: Prompt, terminal: Terminal): LineReader = LineReaderBuilder.builder()
			.appName(prompt.message)
			.terminal(terminal)
			.build()
}
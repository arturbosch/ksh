package io.gitlab.arturbosch.ksh.api

import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
interface ShellBuilder : WithPriority {

	fun createPrompt(): Prompt
	fun createResolver(): Resolver
	fun createTerminal(): Terminal
	fun createLineReader(prompt: Prompt, terminal: Terminal): LineReader
}

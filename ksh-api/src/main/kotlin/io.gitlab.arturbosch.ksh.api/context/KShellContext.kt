package io.gitlab.arturbosch.ksh.api.context

import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.ShellClass
import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
interface KShellContext {

	val prompt: Prompt
	val reader: LineReader
	val terminal: Terminal
	fun commands(): List<ShellClass>
}

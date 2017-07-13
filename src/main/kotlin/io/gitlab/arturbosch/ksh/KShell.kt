package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.context.KShellContext
import org.jline.reader.LineReader
import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
class KShell(val prompt: Prompt,
			 val lineReader: LineReader,
			 val resolver: Resolver) :
		Resolver by resolver,
		LineReader by lineReader,
		Prompt by prompt {

	override val priority: Int = Int.MIN_VALUE
}

class DefaultKShellContext(override val prompt: Prompt,
						   override val reader: LineReader,
						   override val terminal: Terminal) : KShellContext

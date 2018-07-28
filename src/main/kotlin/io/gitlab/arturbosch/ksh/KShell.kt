package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
class KShell(private val prompt: Prompt,
			 private val lineReader: LineReader,
			 private val resolver: Resolver) :
		Resolver by resolver,
		LineReader by lineReader,
		Prompt by prompt {

	override val priority: Int = Int.MIN_VALUE

	fun writeln(msg: String?) = terminal.writeln(msg)
}

class DefaultKShellContext : KShellContext() {

	override val priority: Int = Int.MIN_VALUE

	override fun commands(): List<ShellClass> = loadCommands()
}

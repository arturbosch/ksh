package io.gitlab.arturbosch.ksh.aliases

import io.gitlab.arturbosch.ksh.api.Completer
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.provider.CompleterProvider
import io.gitlab.arturbosch.kutils.Container
import org.jline.reader.Candidate
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

class AliasesCompleter(private val aliases: Set<String>) : Completer {

    override fun init(commands: List<ShellClass>) {
        // nothing to do here
    }

    override fun complete(reader: LineReader?, line: ParsedLine, candidates: MutableList<Candidate>) {
        val subject = line.line()
        aliases.filter { it.startsWith(subject) }
            .forEach { candidates.add(Candidate(it)) }
    }
}

class AliasesCompleterProvider : CompleterProvider {

    override fun provide(container: Container): Completer = AliasesCompleter(aliases(container).keys)
}

package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.Completer
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.kutils.toHashMap
import org.jline.reader.Candidate
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import java.util.WeakHashMap

/**
 * @author Artur Bosch
 */
class DefaultCompleter : Completer {

    private lateinit var possibleCommands: Map<String, ShellClass>
    private val commandMethodCache = WeakHashMap<ShellClass, List<MethodTarget>>()

    override fun init(commands: List<ShellClass>) {
        possibleCommands = commands.toHashMap({ it.commandId }, { it })
    }

    override fun complete(
        reader: LineReader,
        line: ParsedLine,
        candidates: MutableList<Candidate>
    ) {
        val words = line.words().subList(0, line.wordIndex())
        Completer(
            possibleCommands,
            commandMethodCache,
            line,
            candidates
        ).decide(words)
    }

    enum class WordBuffer {
        Empty, OptionStart, Text
    }

    private class Completer(
        val commands: Map<String, ShellClass>,
        val cache: WeakHashMap<ShellClass, List<MethodTarget>>,
        val line: ParsedLine,
        val candidates: MutableList<Candidate>
    ) {

        fun decide(words: List<String>) {
            if (words.isEmpty()) {
                allCommandOptions()
            } else {
                when {
                    words.isEmpty() -> allCommandOptions()
                    words.size == 1 -> optionsForCommand(words[0])
                    words.size == 2 -> optionsForSubCommand(words[0], words[1])
                    else -> moreOptions(words[0], words[1], words.subList(2, words.size))
                }
            }
        }

        private fun fillPossibilities(pos: Collection<String>): Boolean = candidates.addAll(pos.map { Candidate(it) })

        private fun allCommandOptions() = fillPossibilities(commands.keys)

        private fun evaluateCurrentWord(): WordBuffer {
            val word = line.word().substring(0, line.wordCursor())
            return when {
                word.isEmpty() -> WordBuffer.Empty
                word.startsWith("-") -> WordBuffer.OptionStart
                else -> WordBuffer.Text
            }
        }

        private fun optionsForCommand(command: String) {
            val shellClass = commands[command] ?: return
            val methods = cache.getOrPut(shellClass) { shellClass.extractMethods() }
            when (evaluateCurrentWord()) {
                WordBuffer.Empty -> {
                    val options = mutableSetOf<String>()
                    for (method in methods) {
                        if (method.isMain) {
                            options.addAll(method.values)
                            options.addAll(method.allParameterValues())
                        } else {
                            options.add(method.name)
                        }
                    }
                    fillPossibilities(options)
                }
                WordBuffer.OptionStart -> fillPossibilities(methods.find { it.isMain }?.allParameterValues().orEmpty())
                WordBuffer.Text -> {
                    val mainMethod = methods.find { it.isMain }
                    val mainOptions = mainMethod?.values?.filter { it.startsWith(line.word()) }.orEmpty()
                    val methodOptions = methods.map { it.name }.filter { it.startsWith(line.word()) }
                    val mainParameterOptions = mainMethod?.allParameterValues().orEmpty()
                    fillPossibilities(methodOptions + mainOptions + mainParameterOptions)
                }
            }
        }

        private fun optionsForSubCommand(command: String, subCommand: String) {
            val shellClass = commands[command] ?: return
            val methods = cache.getOrPut(shellClass) { shellClass.extractMethods() }
            val called = methods.find { it.name == subCommand } ?: return
            when (evaluateCurrentWord()) {
                WordBuffer.Empty, WordBuffer.OptionStart -> fillPossibilities(called.allParameterValues())
                WordBuffer.Text -> fillPossibilities(called.allParameterValues().filter { it.startsWith(line.word()) })
            }
        }

        private fun moreOptions(command: String, subCommand: String, usedOptions: List<String>) {
            val shellClass = commands[command] ?: return
            val methods = cache.getOrPut(shellClass) { shellClass.extractMethods() }
            val called = methods.find { it.name == subCommand }
                ?: methods.find { it.isMain }
                ?: return
            val freeOptions = called.allParameterValues().filter { it !in usedOptions }
            when (evaluateCurrentWord()) {
                WordBuffer.Empty, WordBuffer.OptionStart -> fillPossibilities(freeOptions)
                WordBuffer.Text -> fillPossibilities(freeOptions.filter { it.startsWith(line.word()) })
            }
        }
    }
}

package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import java.util.WeakHashMap

/**
 * @author Artur Bosch
 */
class DefaultCompleter(commands: Collection<ShellClass>) : Completer {

    private val possibleCommands =
            commands.fold(HashMap<String, ShellClass>()) { acc, cur ->
                acc[cur.commandId] = cur
                acc
            }

    private val commandMethodCache = WeakHashMap<ShellClass, List<MethodTarget>>()

    enum class Buffer {
        Empty, OptionStart, Word
    }

    override fun complete(
        reader: LineReader,
        line: ParsedLine,
        candidates: MutableList<Candidate>
    ) {

        fun fillPossibilities(pos: Collection<String>) = candidates.addAll(pos.map { Candidate(it) })
        fun evaluateBuffer(): Buffer {
            val word = line.word().substring(0, line.wordCursor())
            return when {
                word.isEmpty() -> Buffer.Empty
                word.startsWith("-") -> Buffer.OptionStart
                else -> Buffer.Word
            }
        }

        fun allCommandOptions() = fillPossibilities(possibleCommands.keys)
        fun optionsForCommand(command: String) {
            val shellClass = possibleCommands[command] ?: return
            val methods = commandMethodCache.getOrPut(shellClass) { shellClass.extractMethods() }
            when (evaluateBuffer()) {
                Buffer.Empty -> {
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
                Buffer.OptionStart -> fillPossibilities(methods.find { it.isMain }?.allParameterValues().orEmpty())
                Buffer.Word -> {
                    val mainMethod = methods.find { it.isMain }
                    val mainOptions = mainMethod?.values?.filter { it.startsWith(line.word()) }.orEmpty()
                    val methodOptions = methods.map { it.name }.filter { it.startsWith(line.word()) }
                    val mainParameterOptions = mainMethod?.allParameterValues().orEmpty()
                    fillPossibilities(methodOptions + mainOptions + mainParameterOptions)
                }
            }
        }

        fun optionsForSubCommand(command: String, subCommand: String) {
            val shellClass = possibleCommands[command] ?: return
            val methods = commandMethodCache.getOrPut(shellClass) { shellClass.extractMethods() }
            val called = methods.find { it.name == subCommand } ?: return
            when (evaluateBuffer()) {
                Buffer.Empty, Buffer.OptionStart -> fillPossibilities(called.allParameterValues())
                Buffer.Word -> fillPossibilities(called.allParameterValues().filter { it.startsWith(line.word()) })
            }
        }

        fun moreOptions(command: String, subCommand: String, usedOptions: MutableList<String>) {
            val shellClass = possibleCommands[command] ?: return
            val methods = commandMethodCache.getOrPut(shellClass) { shellClass.extractMethods() }
            val called = methods.find { it.name == subCommand } ?: return
            val freeOptions = called.allParameterValues().filter { it !in usedOptions }
            when (evaluateBuffer()) {
                Buffer.Empty, Buffer.OptionStart -> fillPossibilities(freeOptions)
                Buffer.Word -> fillPossibilities(freeOptions.filter { it.startsWith(line.word()) })
            }
        }

        if (line.words().isEmpty()) {
            allCommandOptions()
        } else {
            val words = line.words().subList(0, line.wordIndex())
            when {
                words.isEmpty() -> allCommandOptions()
                words.size == 1 -> optionsForCommand(words[0])
                words.size == 2 -> optionsForSubCommand(words[0], words[1])
                else -> moreOptions(words[0], words[1], words.subList(2, words.size))
            }
        }

    }
}

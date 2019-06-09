package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.InputLine
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.ksh.api.SimpleInputLine
import io.gitlab.arturbosch.ksh.api.provider.AdditionalHelpProvider
import io.gitlab.arturbosch.ksh.defaults.extractMethods
import io.gitlab.arturbosch.ksh.defaults.hasBoolType
import io.gitlab.arturbosch.ksh.defaults.isBuiltin
import io.gitlab.arturbosch.ksh.defaults.shellMethod
import io.gitlab.arturbosch.ksh.defaults.shellOption
import io.gitlab.arturbosch.kutils.isTrue
import io.gitlab.arturbosch.kutils.load
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Help : ShellClass {

    private var context: Context by Delegates.notNull()
    private var resolvers: List<Resolver> by Delegates.notNull()

    private val helpers by lazy { load<AdditionalHelpProvider>().toList() }

    override fun init(context: Context) {
        this.context = context
        this.resolvers = context.resolvers
    }

    @ShellMethod(help = "Prints this help message.")
    fun main(
        @ShellOption(["", "--command"], defaultValue = "") command: String
    ): String = if (command.isNotBlank()) {
        val input = SimpleInputLine(command)
        val lookup: InputLine = resolvers.find { it.transforms(input) }
            ?.transform(input)
            ?: input

        val shellClass = commandByName(lookup.firstWord())
        val methods = shellClass.extractMethods()

        fun forSubCommand(mainOnly: Boolean = false): String {
            val shellMethod = if (mainOnly) methods[0] else methods.find { it.name == lookup.secondWord() }
                ?: throw IllegalArgumentException("No sub command with name '${lookup.secondWord()}' found.")
            return forSpecificCommand(shellClass, shellMethod)
        }

        if (lookup.size() == 1 || lookup.secondWord().startsWith("-")) {
            if (methods.size == 1 && methods[0].isMain) {
                forSubCommand(mainOnly = true)
            } else {
                shellClass.toHelp() + NL
            }
        } else {
            forSubCommand()
        }
    } else {
        forAllCommands()
    }

    private fun commandByName(command: String) = context.commands()
        .find { it.commandId == command }
        ?: throw IllegalArgumentException("No command with name '$command' found.")

    private fun forSpecificCommand(command: ShellClass, methodTarget: MethodTarget): String {
        val subCommand = methodTarget.shellMethod()
        val commandHelp = if (command.help.isNotEmpty()) " - ${command.help}" else ""
        val subCommandHelp = if (subCommand?.help?.isNotEmpty().isTrue()) " - ${subCommand?.help}" else ""
        val namePart = "NAME\n" +
            "$EIGHT_SPACES${command.commandId}$commandHelp" +
            "\n$EIGHT_SPACES$FOUR_SPACES${methodTarget.name}$subCommandHelp\n\n"
        val synopsisPart = "SYNOPSIS\n" + EIGHT_SPACES + methodTarget.name +
            methodTarget.parameters.joinToString(" ", prefix = " ") {
                val isBool = it.hasBoolType()
                val open = if (!isBool) "[" else ""
                val closed = if (!isBool) "]" else ""
                val value = if (!isBool) " [" + it.type.simpleName.toLowerCase() + "]" else ""
                val name = it.shellOption?.value?.sorted()?.last()
                "$open[$name]$value$closed"
            } + NL

        val optionsPart = if (methodTarget.parameters.isEmpty()) {
            ""
        } else {
            "\nOPTIONS\n" + methodTarget.parameters
                .joinToString("\n$EIGHT_SPACES", prefix = EIGHT_SPACES) { parameter ->
                    val parameterPart = parameter.shellOption?.value
                        ?.sorted()
                        ?.reversed()
                        ?.joinToString(" or ") { if (it.isEmpty()) "[default]" else it } ?: ""
                    parameterPart + " [${parameter.type.simpleName.toLowerCase()}]"
                } + NL
        }
        return namePart + synopsisPart + optionsPart
    }

    private fun forAllCommands(): String {
        val allCommands = context.commands()
        val otherCommands = allCommands.filter { !it.isBuiltin() }
            .joinToString(NL) { FOUR_SPACES + it.toHelp() }
        val builtinCommands = allCommands.filter { it.isBuiltin() }
            .joinToString(NL) { FOUR_SPACES + it.toHelp() }

        var result = ""

        if (helpers.isNotEmpty()) {
            result += helpers.joinToString(NL) { it.provide(context.container) } + NL
        }
        if (otherCommands.isNotBlank()) {
            result += "Available commands:\n\n$otherCommands\n"
        }
        if (otherCommands.isNotBlank() && builtinCommands.isNotBlank()) {
            result += NL
        }
        if (builtinCommands.isNotBlank()) {
            result += "Builtin commands:\n\n$builtinCommands\n"
        }
        return result
    }

    private fun ShellClass.toHelp(): String {
        val methods = extractMethods()
        val main = methods.find { it.isMain }
        val remaining = methods.filter { !it.isMain }
        val starIndicator = if (main != null) "* " else "  "
        val helpMessage = main?.help ?: help
        var command = "$starIndicator$commandId${if (helpMessage.isBlank()) "" else ": $helpMessage"}"
        if (remaining.isNotEmpty()) {
            command += NL + remaining.joinToString(NL) { EIGHT_SPACES + it.toHelp() }
        }
        return command
    }

    private val MethodTarget.help get() = method.shellMethod().help

    private fun MethodTarget.toHelp() =
        "${if (values.isEmpty()) name else values.joinToString(", ") { it }}: $help"
}

private const val NL = "\n"
private const val FOUR_SPACES = "    "
private const val EIGHT_SPACES = FOUR_SPACES + FOUR_SPACES

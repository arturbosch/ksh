package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.defaults.extractMethods
import io.gitlab.arturbosch.ksh.defaults.isBuiltin
import io.gitlab.arturbosch.ksh.defaults.shellMethod
import kotlin.properties.Delegates

/**
 * @author Artur Bosch
 */
@BuiltinCommand
class Help : ShellClass {

	private var context: KShellContext by Delegates.notNull()

	override fun init(context: KShellContext) {
		this.context = context
	}

	@ShellMethod(help = "Prints this help message.")
	fun main(): String {
		val allCommands = context.commands()
		val otherCommands = allCommands.filter { !it.isBuiltin() }
				.joinToString("\n") { FOUR_SPACES + it.toHelp() }
		val builtinCommands = allCommands.filter { it.isBuiltin() }
				.joinToString("\n") { FOUR_SPACES + it.toHelp() }

		var result = ""
		if (otherCommands.isNotBlank()) {
			result += "Available commands:\n\n$otherCommands\n"
		}
		if (otherCommands.isNotBlank() && builtinCommands.isNotBlank()) {
			result += "\n"
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
			command += "\n" + remaining.joinToString("\n") { EIGHT_SPACES + it.toHelp() }
		}
		return command
	}

	private val MethodTarget.help get() = method.shellMethod().help

	private fun MethodTarget.toHelp() =
			"${if (values.isEmpty()) name else values.joinToString(", ") { it }}: ${method.shellMethod().help}"
}

private const val FOUR_SPACES = "    "
private const val EIGHT_SPACES = FOUR_SPACES + FOUR_SPACES

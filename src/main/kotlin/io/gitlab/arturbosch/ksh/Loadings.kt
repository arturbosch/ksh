package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.ShellClass
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */

fun loadPrompt() = ServiceLoader.load(Prompt::class.java, Prompt::class.java.classLoader)
		.sortedBy { it.priority }
		.reversed()
		.firstOrNull()

fun loadCommands() = ServiceLoader.load(ShellClass::class.java).toList()
package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellContext
import io.gitlab.arturbosch.ksh.api.WithPriority
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */

val kshLoader: ClassLoader = Ksh::class.java.classLoader

fun loadShellContext() = ServiceLoader.load(ShellContext::class.java, kshLoader).firstPrioritized()
fun loadPrompt() = ServiceLoader.load(Prompt::class.java, kshLoader).firstPrioritized()
fun loadCommands() = ServiceLoader.load(ShellClass::class.java, kshLoader).toList()
fun loadResolver() = ServiceLoader.load(Resolver::class.java, kshLoader).firstPrioritized()

private fun <T : WithPriority> ServiceLoader<T>.firstPrioritized(): T?
		= sortedBy { it.priority }.reversed().firstOrNull()
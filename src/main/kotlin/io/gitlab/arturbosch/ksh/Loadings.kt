package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.KShellContext
import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.api.Prompt
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.WithPriority
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */

val kshLoader: ClassLoader = Bootstrap::class.java.classLoader

fun loadShellContext() =
        ServiceLoader.load(KShellContext::class.java, kshLoader)
                .firstPrioritized()
            ?: throw IllegalStateException("No KShellContext class provided.")

fun loadShellBuilder() =
        ServiceLoader.load(ShellBuilder::class.java, kshLoader)
                .firstPrioritized()
            ?: throw IllegalStateException("No shell builder provided.")

fun loadPrompt() = ServiceLoader.load(Prompt::class.java, kshLoader).firstPrioritized()
fun loadCommands() = ServiceLoader.load(ShellClass::class.java, kshLoader).toList()
fun loadResolver() = ServiceLoader.load(Resolver::class.java, kshLoader).firstPrioritized()

fun loadParameterResolvers() =
        ServiceLoader.load(ParameterResolver::class.java, kshLoader)
                .sortedBy { it.priority }
                .reversed()
                .toList()

private fun <T : WithPriority> ServiceLoader<T>.firstPrioritized(): T? =
        sortedBy { it.priority }
                .lastOrNull()

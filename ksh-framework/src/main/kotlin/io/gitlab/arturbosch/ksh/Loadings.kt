package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.WithPriority
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */

val kshLoader: ClassLoader = Bootstrap::class.java.classLoader

val loadedCommands by lazy { ServiceLoader.load(ShellClass::class.java, kshLoader).toList() }

fun <T : WithPriority> ServiceLoader<T>.firstPrioritized(): T? =
    sortedBy { it.priority }
        .lastOrNull()

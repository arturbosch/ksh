package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.ShellClass
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
fun loadCommands() = ServiceLoader.load(ShellClass::class.java).toList()
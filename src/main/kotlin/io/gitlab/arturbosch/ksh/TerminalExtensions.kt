package io.gitlab.arturbosch.ksh

import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
fun Terminal.writeln(msg: String?) = writer().write(msg + "\n")

fun KShell.writeln(msg: String?) = terminal.writeln(msg)

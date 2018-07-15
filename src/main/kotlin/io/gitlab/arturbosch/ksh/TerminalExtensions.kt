package io.gitlab.arturbosch.ksh

import org.jline.terminal.Terminal

/**
 * @author Artur Bosch
 */
fun Terminal.writeln(msg: String?) = writer().write(msg + "\n")

fun Terminal.writeln(error: Throwable?) = error?.printStackTrace(writer())

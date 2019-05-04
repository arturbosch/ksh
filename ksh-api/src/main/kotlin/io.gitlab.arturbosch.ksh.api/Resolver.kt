package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority
import org.jline.reader.Completer as JLineCompleter

/**
 * @author Artur Bosch
 */
interface Resolver : WithPriority, WithCommands {

    fun evaluate(input: InputLine): CallTarget
}

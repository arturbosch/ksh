package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority
import io.gitlab.arturbosch.kutils.simpleClassName
import org.jline.reader.Completer as JLineCompleter

/**
 * @author Artur Bosch
 */
interface Resolver : WithPriority, WithCommands {

    fun supports(input: InputLine): Boolean = true
    fun transforms(input: InputLine): Boolean = false
    fun transform(input: InputLine): InputLine =
        throw UnsupportedOperationException("${simpleClassName<Resolver>()} does not support transforming input line.")

    fun evaluate(input: InputLine): CallTarget
}

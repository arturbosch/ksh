package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority
import org.jline.reader.LineReaderBuilder

/**
 * @author Artur Bosch
 */
interface ShellBuilder : WithPriority {

    fun createShell(
        settings: ShellSettings,
        init: (LineReaderBuilder) -> LineReaderBuilder
    ): Shell
}

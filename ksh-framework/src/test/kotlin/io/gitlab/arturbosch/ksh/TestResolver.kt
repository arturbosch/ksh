package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.defaults.DefaultResolver
import io.gitlab.arturbosch.ksh.defaults.JLineInput
import org.jline.reader.impl.DefaultParser

/**
 * @author Artur Bosch
 */
class TestResolver : DefaultResolver() {

    private val parser = DefaultParser()

    fun evaluate(input: String) = evaluate(JLineInput(parser.parse(input, 0)))
}

package io.gitlab.arturbosch.ksh.test

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.defaults.DefaultResolver
import io.gitlab.arturbosch.ksh.defaults.JLineInput
import org.jline.reader.impl.DefaultParser

/**
 * @author Artur Bosch
 */
class TestResolver : DefaultResolver() {

    private val parser = DefaultParser()

    fun evaluate(input: String) = evaluate(JLineInput(parser.parse(input, 0))).invoke()

    override fun init(commands: List<ShellClass>): TestResolver {
        return super.init(commands) as TestResolver
    }
}

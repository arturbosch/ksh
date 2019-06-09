package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.NoopContainer
import io.gitlab.arturbosch.ksh.bootstrap
import io.gitlab.arturbosch.kutils.asPath
import io.gitlab.arturbosch.kutils.exists

/**
 * Runs commands inside a file:
 *  - java -jar ksh-template.jar \[KSH_REPOSITORY\]/ksh-framework/src/test/resources/hello_script
 * Runs a commando from the cli:
 *  - java -jar ksh-template.jar hello
 * Starts the interactive shell:
 *  - java -jar ksh-template.jar
 */
fun main(vararg args: String) {
    if (args.isNotEmpty()) {
        if (args.size == 1 && args.first().asPath().exists()) {
            bootstrap(NoopContainer()).runFile(args.first().asPath())
        } else {
            bootstrap(NoopContainer()).runOnce(args.joinToString(" "))
        }
    } else {
        bootstrap()
    }
}

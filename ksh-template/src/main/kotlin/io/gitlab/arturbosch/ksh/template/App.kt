package io.gitlab.arturbosch.ksh.template

import io.gitlab.arturbosch.ksh.NoopContainer
import io.gitlab.arturbosch.ksh.bootstrap
import io.gitlab.arturbosch.kutils.asPath
import io.gitlab.arturbosch.kutils.exists

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

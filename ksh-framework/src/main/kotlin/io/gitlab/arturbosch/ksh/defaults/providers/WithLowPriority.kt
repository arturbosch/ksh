package io.gitlab.arturbosch.ksh.defaults.providers

import io.gitlab.arturbosch.kutils.WithPriority

interface WithLowPriority : WithPriority {

    @JvmDefault
    override val priority: Int
        get() = Int.MIN_VALUE
}

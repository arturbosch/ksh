package io.gitlab.arturbosch.ksh.api.provider

import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.WithPriority

interface Provider<T> : WithPriority {

    fun provide(container: Container): T
}

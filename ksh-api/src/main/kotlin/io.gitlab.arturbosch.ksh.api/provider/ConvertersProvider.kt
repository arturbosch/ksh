package io.gitlab.arturbosch.ksh.api.provider

import io.gitlab.arturbosch.ksh.api.Converter

interface ConvertersProvider : Provider<List<Converter<*>>>

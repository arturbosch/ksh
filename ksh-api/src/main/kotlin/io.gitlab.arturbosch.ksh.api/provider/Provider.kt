package io.gitlab.arturbosch.ksh.api.provider

import io.gitlab.arturbosch.ksh.api.Completer
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.WithPriority

interface Provider<T> : WithPriority {

    fun provide(container: Injektor): T
}

interface ShellSettingsProvider : Provider<ShellSettings>
interface ShellBuilderProvider : Provider<ShellBuilder>
interface ContextProvider : Provider<Context>
interface ConvertersProvider : Provider<List<Converter<*>>>
interface CompleterProvider : Provider<Completer>
interface ParameterResolverProvider : Provider<ParameterResolver>
interface ResolverProvider : Provider<Resolver>
interface ShellClassesProvider : Provider<List<ShellClass>>

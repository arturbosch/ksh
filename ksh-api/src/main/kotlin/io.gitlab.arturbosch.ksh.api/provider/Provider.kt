package io.gitlab.arturbosch.ksh.api.provider

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.ParameterResolver
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.kutils.Injektor

interface Provider<T> {

    fun provide(container: Injektor): T
}

interface ShellSettingsProvider : Provider<ShellSettings>
interface ShellBuilderProvider : Provider<ShellBuilder>
interface ContextProvider : Provider<Context>
interface ConvertersProvider : Provider<List<Converter<*>>>
interface CompleterProvider : Provider<ParameterResolver>
interface ParameterResolverProvider : Provider<ParameterResolver>
interface ResolverProvider : Provider<Resolver>
interface ShellClassesProvider : Provider<List<ShellClass>>

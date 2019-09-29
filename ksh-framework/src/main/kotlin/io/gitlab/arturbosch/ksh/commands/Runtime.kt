package io.gitlab.arturbosch.ksh.commands

import io.gitlab.arturbosch.ksh.api.BuiltinCommand
import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Converter
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.provider.ConvertersProvider
import io.gitlab.arturbosch.kutils.SimpleStringNode
import io.gitlab.arturbosch.kutils.load
import io.gitlab.arturbosch.kutils.loadProperties
import io.gitlab.arturbosch.kutils.resourceAsStream
import io.gitlab.arturbosch.kutils.single
import io.gitlab.arturbosch.kutils.toTreeString

@BuiltinCommand
class Runtime : ShellClass {

    override val help: String = "Provides meta information about the shell runtime."

    private var context: Context by single()
    private val props by lazy { loadProperties(resourceAsStream("runtime.properties")) }

    override fun init(context: Context) {
        this.context = context
    }

    @ShellMethod
    fun main(): String = version()

    @ShellMethod(help = "Prints ksh-runtime version.")
    fun version(): String = props["version"] ?: "unspecified"

    @ShellMethod(help = "Lists all registered converters.")
    fun converters(): String {
        val converterIds = load<ConvertersProvider>()
            .flatMap { it.provide(context.container) }
            .map { SimpleStringNode(it.identifier(), emptyList()) }
        return toTreeString(SimpleStringNode("Registered converters:", converterIds))
    }

    private fun Converter<*>.identifier(): String = "${this::class.java.name}=${id.java.name}"
}

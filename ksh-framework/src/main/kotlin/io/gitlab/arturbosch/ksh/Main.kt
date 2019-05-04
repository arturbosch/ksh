package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ContextProvider
import io.gitlab.arturbosch.ksh.api.provider.ResolverProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellBuilderProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShell
import io.gitlab.arturbosch.ksh.defaults.DefaultShellBuilder
import io.gitlab.arturbosch.ksh.defaults.DefaultShellSettings
import io.gitlab.arturbosch.kutils.DefaultInjektor
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.TypeReference
import io.gitlab.arturbosch.kutils.addSingleton
import io.gitlab.arturbosch.kutils.load
import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import java.lang.reflect.Type

/**
 * @author Artur Bosch
 */
fun main() = bootstrap()

fun bootstrap() {
    val bootstrap = bootstrap(DefaultInjektor())
    bootstrap.runLooping()
}

fun bootstrap(
    container: Injektor,
    builder: ShellBuilder = object : ShellBuilderProvider {
        override fun provide(container: Injektor): ShellBuilder = DefaultShellBuilder()
    }.provide(container)
): Bootstrap {
    val settings = object : ShellSettingsProvider {
        override fun provide(container: Injektor): ShellSettings = DefaultShellSettings()
    }.provide(container)
    container.addSingleton(settings)
    Debugging.isDebug = settings.debug

    val (term, reader) = builder.createShell(settings) as DefaultShell
    Debugging.terminal = term

    container.addSingleton(term)
    container.addSingleton(reader)

    verify(loadedCommands)
    val resolver = load<ResolverProvider>()
        .firstPrioritized()
        ?.provide(container)
        ?.init(loadedCommands)
        ?: throw IllegalStateException("No resolver found!")
    container.addSingleton(resolver)

    val context = object : ContextProvider {
        override fun provide(container: Injektor): Context = object : Context() {
            override var settings: ShellSettings = settings
            override var reader: LineReader = reader
            override var terminal: Terminal = term
            override var resolver: Resolver = resolver
            override fun commands(): List<ShellClass> = loadedCommands
        }
    }.provide(container)

    container.addSingleton(context)
    loadedCommands.forEach { it.init(context) }
    return Bootstrap(context)
}

private operator fun DefaultShell.component2(): LineReader = this.lineReader
private operator fun DefaultShell.component1(): Terminal = this.terminal

class NoopContainer : Injektor {

    override fun <T : Any> addFactory(typeReference: TypeReference<T>, factory: () -> T) = Unit
    override fun <T : Any> addSingletonFactory(typeReference: TypeReference<T>, factory: () -> T) = Unit
    override fun <T : Any> get(type: Type): T = throw UnsupportedOperationException("not implemented")
}

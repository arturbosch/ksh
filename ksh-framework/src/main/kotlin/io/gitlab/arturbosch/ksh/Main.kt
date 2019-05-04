package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ContextProvider
import io.gitlab.arturbosch.ksh.api.provider.ResolverProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellBuilderProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShell
import io.gitlab.arturbosch.ksh.defaults.DefaultShellBuilder
import io.gitlab.arturbosch.ksh.defaults.DefaultShellSettings
import io.gitlab.arturbosch.kutils.DefaultInjektor
import io.gitlab.arturbosch.kutils.Injektor
import io.gitlab.arturbosch.kutils.TypeReference
import io.gitlab.arturbosch.kutils.addSingleton
import io.gitlab.arturbosch.kutils.load
import io.gitlab.arturbosch.kutils.withSingleton
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
    builder: ShellBuilder =
        load<ShellBuilderProvider>()
            .firstPrioritized()
            ?.provide(container)
            ?: DefaultShellBuilder()
): Bootstrap {
    val settings = load<ShellSettingsProvider>()
        .firstPrioritized()
        ?.provide(container)
        ?: DefaultShellSettings()
    container.addSingleton(settings)
    Debugging.isDebug = settings.debug

    val (term, reader) = builder.createShell(settings) as DefaultShell
    Debugging.terminal = term

    container.addSingleton(term)
    container.addSingleton(reader)

    val loadedCommands = load<ShellClassesProvider>().flatMap { it.provide(container) }
    verify(loadedCommands)

    val context = load<ContextProvider>().firstPrioritized()?.provide(container)
        ?: object : ContextProvider {
            override fun provide(container: Injektor): Context = object : Context() {
                override val priority: Int = Int.MIN_VALUE
                override var settings: ShellSettings = settings
                override var reader: LineReader = reader
                override var terminal: Terminal = term
                override var resolver: Resolver = load<ResolverProvider>()
                    .firstPrioritized()
                    ?.provide(container)
                    ?.init(loadedCommands)
                    ?: throw IllegalStateException("No resolver found!")

                override fun commands(): List<ShellClass> = loadedCommands
            }
        }.provide(container)

    container.withSingleton(context.resolver).init(loadedCommands)
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

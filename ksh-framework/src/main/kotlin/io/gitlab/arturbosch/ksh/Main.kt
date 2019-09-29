package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.Context
import io.gitlab.arturbosch.ksh.api.Resolver
import io.gitlab.arturbosch.ksh.api.ShellBuilder
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.CompleterProvider
import io.gitlab.arturbosch.ksh.api.provider.ContextProvider
import io.gitlab.arturbosch.ksh.api.provider.ResolverProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellBuilderProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.ksh.defaults.DefaultShell
import io.gitlab.arturbosch.kutils.Container
import io.gitlab.arturbosch.kutils.DefaultContainer
import io.gitlab.arturbosch.kutils.TypeReference
import io.gitlab.arturbosch.kutils.addSingleton
import io.gitlab.arturbosch.kutils.firstPrioritized
import io.gitlab.arturbosch.kutils.load
import io.gitlab.arturbosch.kutils.withSingleton
import org.jline.reader.LineReader
import org.jline.reader.impl.completer.AggregateCompleter
import org.jline.terminal.Terminal
import java.lang.reflect.Type

fun main() = bootstrap()

fun bootstrap() {
    val bootstrap = bootstrap(DefaultContainer())
    bootstrap.runLooping()
}

@JvmOverloads
fun bootstrap(
    container: Container = DefaultContainer(),
    builder: ShellBuilder =
        load<ShellBuilderProvider>()
            .firstPrioritized()
            ?.provide(container)
            ?: error("no ShellBuilder provided")
): Bootstrap {
    val settings = load<ShellSettingsProvider>()
        .firstPrioritized()
        ?.provide(container)
        ?: error("no ShellSettings provided")
    container.addSingleton(settings)

    val completers = load<CompleterProvider>().map { it.provide(container) }
    check(completers.isNotEmpty()) { "no Completer provided" }
    val completer = if (completers.size > 1) AggregateCompleter(completers) else completers[0]

    val (term, reader) =
        builder.createShell(settings) {
            it.completer(completer)
        } as DefaultShell

    val debug = container.withSingleton(Debugging(settings.debug, term))

    container.addSingleton(term)
    container.addSingleton(reader)

    val loadedCommands = load<ShellClassesProvider>().flatMap { it.provide(container) }
    debug.log { loadedCommands }
    verify(loadedCommands)

    val resolvers = load<ResolverProvider>()
        .map { it.provide(container) }
        .sortedBy { it.priority }
        .reversed()

    check(resolvers.isNotEmpty()) { "No resolver found!" }

    val context = load<ContextProvider>().firstPrioritized()?.provide(container)
        ?: object : ContextProvider {
            override fun provide(container: Container): Context = object : Context {
                override val priority: Int = Int.MIN_VALUE
                override var container: Container = container
                override var settings: ShellSettings = settings
                override var reader: LineReader = reader
                override var terminal: Terminal = term
                override var resolvers: List<Resolver> = resolvers
                override var lastExceptionState: Throwable? = null
                override fun commands(): List<ShellClass> = loadedCommands
            }
        }.provide(container)

    resolvers.forEach { container.withSingleton(it).init(loadedCommands) }
    completers.forEach { it.init(loadedCommands) }
    container.withSingleton(completer)
    container.addSingleton(context)
    loadedCommands.forEach { it.init(context) }
    return Bootstrap(context)
}

class NoopContainer : Container {

    override fun <T : Any> addFactory(typeReference: TypeReference<T>, factory: () -> T) = Unit
    override fun <T : Any> addSingletonFactory(typeReference: TypeReference<T>, factory: () -> T) = Unit
    override fun <T : Any> get(type: Type): T = throw UnsupportedOperationException("not implemented")
}

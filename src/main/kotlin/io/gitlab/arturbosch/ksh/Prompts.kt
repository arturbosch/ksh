package io.gitlab.arturbosch.ksh

import org.kohsuke.MetaInfServices
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
interface Prompt {
	val message: String
	val priority: Int get() = 0
}

@MetaInfServices
class DefaultPrompt : Prompt {
	override val message: String = "ksh> "
	override val priority: Int get() = -1
}

fun loadPrompt() = ServiceLoader.load(Prompt::class.java, Prompt::class.java.classLoader)
		.sortedBy { it.priority }
		.reversed()
		.firstOrNull()

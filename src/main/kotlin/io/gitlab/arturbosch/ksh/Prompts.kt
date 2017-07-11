package io.gitlab.arturbosch.ksh

import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
interface WithPriority {
	val priority: Int get() = 0
}

interface Prompt : WithPriority {
	val message: String
}

class DefaultPrompt : Prompt {
	override val message: String = "ksh> "
	override val priority: Int get() = -1
}

fun loadPrompt() = ServiceLoader.load(Prompt::class.java, Prompt::class.java.classLoader)
		.sortedBy { it.priority }
		.reversed()
		.firstOrNull()

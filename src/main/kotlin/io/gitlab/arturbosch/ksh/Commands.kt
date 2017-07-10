package io.gitlab.arturbosch.ksh

import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Command

interface CommandProvider

class HelloWorld : CommandProvider {

	@Command
	fun hello(): String = "Hello World!"
}

fun loadCommands() = ServiceLoader.load(CommandProvider::class.java).toList()
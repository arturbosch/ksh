package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class DefaultMethodTarget(override val values: Set<String>,
							   override val method: Method,
							   override val isMain: Boolean = false,
							   override val supportsOptionless: Boolean = false) : MethodTarget {

	override fun invoke(command: ShellClass, arguments: List<Any?>): Any? {
		println("Invoking '${method.name}' on '${command.javaClass.simpleName}' with args='$arguments'")
		return method.invoke(command, *arguments.toTypedArray()) // must be vararg
	}
}

fun Method.toShellMethod(): MethodTarget {
	val isMain = this.name == "main"
	val ann = this.shellMethod()
	val values = ann.value.toSet()
	val optionless = values.isEmpty() || "" in values
	return DefaultMethodTarget(values, this, isMain, optionless)
}

fun Method.shellMethod(): ShellMethod = getAnnotation(ShellMethod::class.java)
		?: throw IllegalStateException("method '${this.name}' misses shell annotation")

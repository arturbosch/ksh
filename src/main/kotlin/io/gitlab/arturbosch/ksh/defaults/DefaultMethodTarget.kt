package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class DefaultMethodTarget(val method: Method,
							   val command: ShellClass,
							   val args: List<Any?> = emptyList()) : MethodTarget {

	override fun invoke(): Any? {
		println("Invoking '${method.name}' on '${command.javaClass.simpleName}' with args='$args'")
		return method.invoke(command, *args.toTypedArray()) // must be vararg
	}
}

package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import java.lang.reflect.Method
import java.util.Arrays

/**
 * @author Artur Bosch
 */
data class DefaultMethodTarget(val method: Method,
							   val command: ShellClass,
							   val args: Array<Any> = emptyArray()) : MethodTarget {

	override fun invoke(): Any? {
		println("Invoking '${method.name}' on '${command.javaClass.simpleName}' with args='${Arrays.toString(args)}'")
		return method.invoke(command, *args)
	}
}
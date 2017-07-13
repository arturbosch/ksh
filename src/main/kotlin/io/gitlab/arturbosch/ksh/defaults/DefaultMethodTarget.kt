package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class DefaultMethodTarget(val method: Method,
							   val command: ShellClass,
							   val args: Array<String> = emptyArray()) : MethodTarget {

	override fun invoke(): Any = method.invoke(command, *args)
}
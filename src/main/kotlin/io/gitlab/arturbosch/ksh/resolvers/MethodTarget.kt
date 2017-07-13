package io.gitlab.arturbosch.ksh.resolvers

import io.gitlab.arturbosch.ksh.api.ShellClass
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class MethodTarget(val method: Method,
						val command: ShellClass,
						val args: Array<String> = emptyArray()) {

	fun invoke(): Any = method.invoke(command, *args)
}
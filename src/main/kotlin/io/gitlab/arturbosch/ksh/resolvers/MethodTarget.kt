package io.gitlab.arturbosch.ksh.resolvers

import io.gitlab.arturbosch.ksh.api.CommandProvider
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class MethodTarget(val method: Method,
						val command: CommandProvider,
						val args: Array<String> = emptyArray()) {

	fun invoke(): Any = method.invoke(command, *args)
}
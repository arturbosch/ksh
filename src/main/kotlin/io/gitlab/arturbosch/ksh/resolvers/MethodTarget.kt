package io.gitlab.arturbosch.ksh.resolvers

import io.gitlab.arturbosch.ksh.api.CommandProvider
import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class MethodTarget(val method: Method,
						val command: CommandProvider,
						val args: Array<String> = emptyArray()) {

	fun invoke() {
		val result = method.invoke(command, args)
		println(result)
	}

	companion object {
		fun of(method: Method?,
			   command: CommandProvider?,
			   args: Array<String> = emptyArray()): MethodTarget? {
			if (method == null || command == null) {
				return null
			} else {
				return MethodTarget(method, command, args)
			}
		}
	}
}
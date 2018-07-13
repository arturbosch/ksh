package io.gitlab.arturbosch.ksh.api

import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
interface MethodTarget {

	val name: String get() = method.name
	val parameters: Array<Parameter> get() = method.parameters
	val values: Set<String>
	val method: Method
	val isMain: Boolean get() = false
	val supportsOptionless: Boolean get() = false

	fun invoke(command: ShellClass, arguments: List<Any?>): Any? {
		return method.invoke(command, *arguments.toTypedArray()) // must be vararg
	}

	fun hasValue(value: String) = value in values ||
			(supportsOptionless && value == name)
}

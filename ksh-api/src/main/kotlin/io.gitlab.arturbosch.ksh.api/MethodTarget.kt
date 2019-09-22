package io.gitlab.arturbosch.ksh.api

import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @author Artur Bosch
 */
interface MethodTarget {

    @JvmDefault
    val name: String
        get() = method.name

    @JvmDefault
    val parameters: Array<Parameter>
        get() = method.parameters

    val values: Set<String>

    val method: Method

    @JvmDefault
    val isMain: Boolean
        get() = false

    @JvmDefault
    val supportsOptionless: Boolean
        get() = false

    @JvmDefault
    fun invoke(command: ShellClass, arguments: List<Any?>): Any? {
        return method.invoke(command, *arguments.toTypedArray()) // must be vararg
    }

    @JvmDefault
    fun hasValue(value: String) = value in values || (supportsOptionless && value == name)
}

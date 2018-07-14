package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.MethodTarget
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import io.gitlab.arturbosch.ksh.api.ShellOptions
import java.lang.reflect.Method
import java.lang.reflect.Parameter


val Parameter.shellOption: ShellOption? get() = getAnnotation(ShellOption::class.java)

fun Parameter.defaultValue(): String = shellOption?.defaultValue ?: ShellOptions.NULL_DEFAULT

fun Parameter.isUnnamedOption(): Boolean = shellOption?.value?.let { "" in it } ?: false

fun ShellClass.extractMethods() = javaClass
		.declaredMethods
		.filter { it.isAnnotationPresent(ShellMethod::class.java) }
		.map { it.toShellMethod() }

fun Method.parameterPrefix() = getAnnotation(ShellMethod::class.java)?.prefix
		?: throw IllegalStateException("Method with ShellMethod annotation expected.")

fun MethodTarget.lookupParameter(word: String, prefix: String): Parameter {
	for (parameter in method.parameters) {
		if (parameter.prefixedValues(prefix).any { it == word }) {
			return parameter
		}
	}
	throw IllegalArgumentException("Could not look up parameter for '$prefix$word' in $method")
}

fun Parameter.arity(): Int {
	val option = getAnnotation(ShellOption::class.java)
	val inferred = if (hasBoolType()) 0 else 1
	return if (option != null && option.arity != ShellOptions.EXTRACT_ARITY) option.arity else inferred
}

fun Parameter.hasBoolType() =
		type == Boolean::class.javaPrimitiveType || type == Boolean::class.java

fun Parameter.prefixedValues(prefix: String): Set<String> {
	val option = shellOption
	return if (option != null && option.value.isNotEmpty()) {
		option.value.toSet()
	} else {
		setOf(prefix + name)
	}
}

package io.gitlab.arturbosch.ksh.defaults.resolvers

import java.lang.reflect.Method

/**
 * @author Artur Bosch
 */
data class MethodSignature(val method: Method,
						   val args: Array<Any> = emptyArray())
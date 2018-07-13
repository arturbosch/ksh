package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
data class CallTarget(val shellClass: ShellClass,
					  val methodTarget: MethodTarget,
					  val arguments: List<Any?> = emptyList()) {

	fun invoke(): Any? = methodTarget.invoke(shellClass, arguments)
}

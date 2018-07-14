package io.gitlab.arturbosch.ksh.api

/**
 * @author Artur Bosch
 */
interface InputLine {

	var parameterStartIndex: Int
	fun firstWord(): String = if (words().isEmpty()) "" else words()[0]
	fun secondWord(): String = if (words().size < 2) "" else words()[1]
	fun words(): List<String>
	fun markParametersStartAfter(word: String)
	fun size(): Int = words().size
}

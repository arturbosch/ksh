package io.gitlab.arturbosch.ksh.defaults

import io.gitlab.arturbosch.ksh.api.InputLine
import org.jline.reader.ParsedLine

/**
 * @author Artur Bosch
 */
class JLineInput(private val parsedLine: ParsedLine) : InputLine {

	override var parameterStartIndex: Int = 0

	override fun markParametersStartAfter(word: String) {
		val (index, _) = words().withIndex()
				.find { (_, value) -> value == word }
				?: throw IllegalArgumentException("word must be inside input line")
		parameterStartIndex = index + 1
	}

	override fun words(): List<String> = parsedLine.words()
}

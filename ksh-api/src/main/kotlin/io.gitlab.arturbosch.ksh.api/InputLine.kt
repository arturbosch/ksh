package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.StringRepresentation

/**
 * @author Artur Bosch
 */
interface InputLine : StringRepresentation {

    var parameterStartIndex: Int
    fun firstWord(): String = if (words().isEmpty()) "" else words()[0]
    fun secondWord(): String = if (words().size < 2) "" else words()[1]
    fun words(): List<String>
    fun markParametersStartAfter(word: String)
    fun size(): Int = words().size
}

class SimpleInputLine(line: String) : InputLine {

    private val words = line.split(" ")

    override var parameterStartIndex: Int = 0
    override fun words(): List<String> = words

    override fun markParametersStartAfter(word: String) {
        val (index, _) = words().withIndex()
            .find { (_, value) -> value == word }
            ?: throw IllegalArgumentException("word must be inside input line")
        parameterStartIndex = index + 1
    }

    override fun toString(): String = "Line(${words.joinToString(" ")})"
}

package io.gitlab.arturbosch.ksh.commands

import com.beust.jcommander.Parameter
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.context.KShellContext
import org.jline.reader.LineReader
import java.util.Random

/**
 * @author Artur Bosch
 */
class play : ShellClass {

	private lateinit var reader: LineReader

	override fun init(context: KShellContext) {
		reader = context.reader
	}

	class Game {
		@Parameter
		var tries: String? = null
	}

	private val random = Random(java.lang.System.currentTimeMillis())

	@ShellMethod
	fun guess(game: Game) {
		val tries = game.tries?.toInt() ?: 1
		println("Guess my number! (0-9)")
		val expected = random.nextInt(10)
		for (i in 1..tries) {
			val guess = reader.readLine("guess> ").toInt()
			if (guess == expected) {
				println("You have guessed my number $expected!")
				return
			}
		}
		print("Sorry, no luck today. My number was $expected!")
	}
}

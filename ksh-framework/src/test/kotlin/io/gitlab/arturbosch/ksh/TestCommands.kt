package io.gitlab.arturbosch.ksh

import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption
import java.io.File
import java.nio.file.Path

class Hello : ShellClass {

    override val commandId: String = "hello"

    @ShellMethod
    fun main(@ShellOption(["--name", "-n", ""]) name: String?): String =
        "Hello ${name ?: "World"}!"

    @ShellMethod
    fun say(@ShellOption(["--name", "-n", ""], defaultValue = "OMG") name: String): String =
        "Hello $name!"

    @ShellMethod
    fun count(
        @ShellOption(["--name", "-n", ""], defaultValue = "OMG") name: String,
        @ShellOption(["--times"], defaultValue = "3") amount: Int
    ): String {
        return "Hello " + (1..amount).joinToString("") { name }
    }

    @ShellMethod
    fun invalid(
        @ShellOption([""]) name: String,
        @ShellOption([""]) age: Int
    ): String = "Hello $name - $age"
}

class Conversions : ShellClass {

    override val commandId: String = "conversions"

    @ShellMethod
    fun main(
        @ShellOption(["-i"]) aInt: Int,
        @ShellOption(["-f"]) aFloat: Float,
        @ShellOption(["-b"]) aBool: Boolean,
        @ShellOption(["-d"]) aDouble: Double,
        @ShellOption(["-s"]) aString: String,
        @ShellOption(["-file"]) aFile: File
    ): String {
        return "$aInt $aFloat $aBool $aDouble $aString $aFile"
    }

    @ShellMethod
    fun path(@ShellOption(["", "-p"]) path: Path) = path
}

class Gradle : ShellClass {

    @Suppress("UNUSED_PARAMETER")
    @ShellMethod
    fun module(
        @ShellOption(["", "--name"]) name: String,
        @ShellOption(["-g", "--group"]) group: String,
        @ShellOption(["-l", "--language"], defaultValue = "kotlin") language: String,
        @ShellOption(["-kts", "--kotlin-dsl"], arity = 0, defaultValue = "false") kts: Boolean,
        @ShellOption(["-wd", "--working-dir"], defaultValue = ".") workingDir: String
    ): String = ""
}

class DoubleMain : ShellClass {

    override val commandId: String = "double"

    @ShellMethod
    fun main() = "first"

    @ShellMethod(value = ["main"])
    fun secondMain() = "second"
}

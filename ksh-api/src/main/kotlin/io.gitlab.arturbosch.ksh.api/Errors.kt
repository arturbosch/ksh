package io.gitlab.arturbosch.ksh.api

import java.lang.reflect.Parameter

/**
 * Marker exception with symbolic meaning that this error is recoverable and won't crash the shell.
 * The 'stacktrace' command will print the last thrown runtime error.
 */
open class ShellException(msg: String?, cause: Throwable? = null) : RuntimeException(msg, cause)

/**
 * Marker exception which indicates the ksh engine to stop with given exit code.
 */
class ExitShell(val exitCode: Int = 0) : java.lang.RuntimeException()

/**
 * Is thrown when no registered [ParameterResolver] is able to resolve the [Parameter].
 * Registering a [Converter] for given parameter type could solve this error.
 */
class UnsupportedParameter(parameters: Collection<Parameter>) :
        ShellException("Could not convert/resolve: $parameters.")

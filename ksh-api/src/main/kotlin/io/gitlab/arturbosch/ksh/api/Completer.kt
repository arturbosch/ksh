package io.gitlab.arturbosch.ksh.api

import io.gitlab.arturbosch.kutils.WithPriority
import org.jline.reader.Completer

interface Completer : Completer, WithPriority, WithCommands

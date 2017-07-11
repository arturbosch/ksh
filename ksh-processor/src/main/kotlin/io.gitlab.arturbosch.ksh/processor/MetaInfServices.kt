package io.gitlab.arturbosch.ksh.processor

import java.io.InputStream
import java.io.OutputStream

/**
 * @author Artur Bosch
 */
const val SERVICES_PATH = "META-INF/services"

fun getPath(serviceName: String) = SERVICES_PATH + "/" + serviceName

private val HASH = "#"

fun readServices(input: InputStream): List<String> = input.bufferedReader().use {
	val classes = mutableListOf<String>()
	var line = it.readLine()
	while (line != null) {
		if (line.contains(HASH)) {
			val serviceLine = line.substringBefore(HASH).trim()
			if (serviceLine.isNotEmpty()) {
				classes.add(serviceLine)
			}
		}
		line = readLine()
	}
	return classes
}

fun writeServices(services: List<String>, out: OutputStream) = out.bufferedWriter().use { writer ->
	services.forEach { writer.write(it); writer.newLine() }
	writer.flush()
}
package io.github.msengbusch.unitsystem.util

import io.github.msengbusch.unitsystem.exception.UnitException
import java.io.BufferedReader
import java.io.InputStreamReader

fun contentFromResource(path: String): List<String> {
    val url = ClassLoader.getSystemClassLoader().getResource(path)
        ?: throw UnitException("Failed to load file from jar: $path. Notify the author")

    val reader = BufferedReader(InputStreamReader(url.openStream()))
    val lines = reader.readLines()

    reader.close()
    return lines
}
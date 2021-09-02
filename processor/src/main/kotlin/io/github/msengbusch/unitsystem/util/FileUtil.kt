package io.github.msengbusch.unitsystem.util

import java.io.BufferedWriter
import java.io.OutputStreamWriter
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.StandardLocation

fun ProcessingEnvironment.writeToResource(path: String, content: String) {
    val outputStream = BufferedWriter(
        OutputStreamWriter(
            filer.createResource(StandardLocation.SOURCE_OUTPUT, "", path).openOutputStream()
        )
    )
    outputStream.write(content)
    outputStream.close()
}
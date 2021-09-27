package io.github.msengbusch.unitsystem.extension

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

object ExtensionOutput {
    fun generateOutput(extensions: List<PreExtension>, generator: CodeGenerator, logger: KSPLogger) {
        extensions.forEach { generateExtensionFile(it, generator, logger) }
        generateOutputFile(extensions, generator, logger)
    }

    private fun generateOutputFile(extensions: List<PreExtension>, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate extensions file for ${extensions.joinToString { it.className }}")

        val writer = generator.createNewFile(
            Dependencies(true, *extensions.map { it.file }.toTypedArray()),
            "unit",
            "extensions",
            ""
        ).bufferedWriter()
        extensions.forEach { writer.write("${it.className}\n") }
        writer.close()
    }

    private fun generateExtensionFile(extension: PreExtension, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate extension file for $extension")

        val writer = generator.createNewFile(
            Dependencies(true, extension.file),
            "unit",
            extension.className.replace(".", "/"),
            "extension"
        ).bufferedWriter()

        writer.write("class=${extension.className}\n")

        writer.close()
    }
}
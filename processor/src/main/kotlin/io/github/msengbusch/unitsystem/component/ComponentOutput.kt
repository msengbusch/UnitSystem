package io.github.msengbusch.unitsystem.component

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

object ComponentOutput {
    fun generateOutput(extensions: List<PreComponent>, generator: CodeGenerator, logger: KSPLogger) {
        extensions.forEach { generateExtensionFile(it, generator, logger) }
        generateOutputFile(extensions, generator, logger)
    }

    private fun generateOutputFile(components: List<PreComponent>, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate components file for ${components.joinToString { it.className }}")

        val writer = generator.createNewFile(
            Dependencies(true, *components.map { it.file }.toTypedArray()),
            "unit",
            "components",
            ""
        ).bufferedWriter()
        components.forEach { writer.write("${it.className}\n") }
        writer.close()
    }

    private fun generateExtensionFile(component: PreComponent, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate component file for $component")

        val writer = generator.createNewFile(
            Dependencies(true, component.file),
            "unit",
            component.className.replace(".", "/"),
            "component"
        ).bufferedWriter()

        writer.write("class=${component.className}\n")

        writer.close()
    }
}
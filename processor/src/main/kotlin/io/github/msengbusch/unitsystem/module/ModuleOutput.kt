package io.github.msengbusch.unitsystem.module

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

object ModuleOutput {
    fun generateOutput(modules: List<PreModule>, generator: CodeGenerator, logger: KSPLogger) {
        modules.forEach { generateModuleFile(it, generator, logger) }
        generateOutputFile(modules, generator, logger)
    }

    private fun generateOutputFile(modules: List<PreModule>, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate module file for ${modules.joinToString { it.className }}")

        val writer = generator.createNewFile(
            Dependencies(true, *modules.map { it.file }.toTypedArray()),
            "unit",
            "modules",
            ""
        ).bufferedWriter()
        modules.forEach { writer.write("${it.className}\n") }
        writer.close()
    }

    private fun generateModuleFile(module: PreModule, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate module file for $module")

        val writer = generator.createNewFile(
            Dependencies(true, module.file),
            "unit",
            module.className.replace(".", "/"),
            "module"
        ).bufferedWriter()

        writer.write("class=${module.className}\n")

        writer.close()
    }
}
package io.github.msengbusch.unitsystem.unit

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFile

object UnitOutput {
    fun generateUnitsOutput(units: List<ValidUnit>, generator: CodeGenerator, logger: KSPLogger) {
        generateUnitsFile(units, generator, logger)

        units.forEach { unit ->
            generateUnitFile(unit, generator, logger)
        }
    }

    private fun generateUnitsFile(units: List<ValidUnit>, generator: CodeGenerator, logger: KSPLogger) {
        val allSourceFiles: List<KSFile> = units.map { it.file }

        logger.info("Generate units file for ${units.joinToString(", ") { it.name }}")

        val writer = generator.createNewFile(
            Dependencies(true, *allSourceFiles.toTypedArray()),
            "unitsystem", "units", ""
        )
            .bufferedWriter()

        units.forEach { unit ->
            writer.write("${unit.name}\n")
        }

        writer.close()
    }

    private fun generateUnitFile(unit: ValidUnit, generator: CodeGenerator, logger: KSPLogger) {
        logger.info("Generate unit file for $unit")

        val writer = generator.createNewFile(
            Dependencies(true, unit.file), "unitsystem",
            unit.name, "unit"
        )
            .bufferedWriter()

        writer.write("name=${unit.name}\n")
        writer.write("class=${unit.className}\n")
        writer.write("component=${unit.isComponent}\n")
        writer.write("inheritable=${unit.isInheritable}\n")
        writer.write("instanciable=${unit.isInstanciable}\n")
        if (unit.before.isNotEmpty()) writer.write("before=${unit.before.joinToString(",") { it.name }}\n")
        if (unit.after.isNotEmpty()) writer.write("after=${unit.after.joinToString(",") { it.name }}\n")
        if (unit.inherited.isNotEmpty()) writer.write("inherited=${unit.inherited.joinToString(",") { it.name }}\n")
        if (unit.components.isNotEmpty()) writer.write("components=${unit.components.joinToString(",") { it.name }}\n")

        writer.close()
    }
}
package io.github.msengbusch.unitsystem.output

import io.github.msengbusch.unitsystem.declaration.UnitDeclaration
import java.nio.file.Files
import java.nio.file.Path

object UnitFile {
    fun writeUnitFile(file: Path, units: List<UnitDeclaration>) {
        val lines = mutableListOf<String>()

        units.forEach { appendUnit(it, lines) }

        Files.write(file, lines)
    }

    private fun appendUnit(unit: UnitDeclaration, lines: MutableList<String>) {
        lines.add("[Unit]")
        lines.add("Name=${unit.name}")
    }
}
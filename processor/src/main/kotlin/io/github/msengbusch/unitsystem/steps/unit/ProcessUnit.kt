package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.steps.event.ScanEvent

data class ProcessUnit(val scanUnit: ScanUnit, val events: List<ScanEvent>) {
    val name = scanUnit.name
    val className = scanUnit.className
}
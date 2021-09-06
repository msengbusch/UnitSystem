package io.github.msengbusch.unitsystem.steps.unit.data

import io.github.msengbusch.unitsystem.steps.event.data.RawEvent

data class ValidUnit(private val rawUnit: RawUnit, val before: List<RawUnit>, val after: List<RawUnit>, val events: List<RawEvent>) {
    val name = rawUnit.name
    val className = rawUnit.className
}
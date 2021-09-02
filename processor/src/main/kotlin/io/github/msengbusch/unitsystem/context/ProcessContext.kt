package io.github.msengbusch.unitsystem.context

import io.github.msengbusch.unitsystem.steps.event.FoundUnitEvent
import io.github.msengbusch.unitsystem.steps.unit.FoundUnit

class ProcessContext {
    val unitEvents: MutableMap<String, FoundUnitEvent> = mutableMapOf()
    val units: MutableMap<String, FoundUnit> = mutableMapOf()
}
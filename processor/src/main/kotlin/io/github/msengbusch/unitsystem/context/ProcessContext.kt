package io.github.msengbusch.unitsystem.context

import io.github.msengbusch.unitsystem.steps.event.UnitEvent

class ProcessContext {
    val unitEvents: MutableMap<String, UnitEvent> = mutableMapOf()
}
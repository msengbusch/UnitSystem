package io.github.msengbusch.unitsystem.context

import io.github.msengbusch.unitsystem.steps.event.FoundUnitEvent

class ScanContext {
    val foundUnitEvents = mutableMapOf<String, FoundUnitEvent>()
}
package io.github.msengbusch.unitsystem.context

import io.github.msengbusch.unitsystem.steps.event.ScanEvent
import io.github.msengbusch.unitsystem.steps.unit.ScanUnit

class ScanContext {
    val events = mutableMapOf<String, ScanEvent>()
    val units = mutableMapOf<String, ScanUnit>()
}
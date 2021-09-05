package io.github.msengbusch.unitsystem.context

import io.github.msengbusch.unitsystem.steps.unit.ProcessUnit

class ProcessContext {
    val units = mutableMapOf<String, ProcessUnit>()
}
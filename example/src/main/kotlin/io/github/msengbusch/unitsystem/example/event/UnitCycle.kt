package io.github.msengbusch.unitsystem.example.event

import io.github.msengbusch.unitsystem.event.UnitEvent

@UnitEvent
interface UnitCycle {
    fun enable()
    fun disable()
}
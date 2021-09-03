package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.Unit
import io.github.msengbusch.unitsystem.example.event.UnitCycle

@Unit("testUnit")
class TestUnit : UnitCycle {
    override fun enable() {
        println("Test unit enable")
    }

    override fun disable() {
        println("Test unit  ")
    }
}
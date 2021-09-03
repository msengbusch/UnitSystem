package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.unit.Unit

@Unit("testUnit")
class TestUnit : UnitCycle {
    override fun enable() {
        println("Test unit enable")
    }

    override fun disable() {
        println("Test unit  ")
    }
}
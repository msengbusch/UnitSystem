package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.Unit
import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.example.event.UnitReload

@Unit("testUnit")
class TestUnit3 : UnitCycle, UnitReload {
    override fun enable() {
        println("Test unit enable")
    }

    override fun disable() {
        println("Test unit disable")
    }
}
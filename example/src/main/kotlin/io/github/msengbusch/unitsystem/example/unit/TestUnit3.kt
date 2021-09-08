package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.example.event.UnitReload
import io.github.msengbusch.unitsystem.old.unit.Unit
import javax.inject.Inject

@Unit("testUnit3")
class TestUnit3 @Inject constructor() : UnitCycle, UnitReload {
    override fun enable() {
        println("Test unit 3 enable")
    }

    override fun reload() {
        println("This is a test unit reload")
    }

    override fun disable() {
        println("Test unit 3 disable")
    }
}
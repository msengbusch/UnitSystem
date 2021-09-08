package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.old.unit.Unit
import javax.inject.Inject

@Unit("testUnit4")
class TestUnit4 @Inject constructor() : UnitCycle {
    override fun enable() {
        println("Test unit 4 enable")
    }

    override fun disable() {
        println("Test unit 4 disable")
    }
}
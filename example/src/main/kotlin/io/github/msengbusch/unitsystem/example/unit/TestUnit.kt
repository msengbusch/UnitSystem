package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.old.unit.Unit
import javax.inject.Inject

@Unit("testUnit", before = ["testUnit2"])
class TestUnit @Inject constructor() : UnitCycle {
    override fun enable() {
        println("Test unit enable")
    }

    override fun disable() {
        println("Test unit disable")
    }
}
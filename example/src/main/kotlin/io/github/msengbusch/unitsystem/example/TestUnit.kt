package io.github.msengbusch.unitsystem.example

import io.github.msengbusch.unitsystem.Unit

@Unit("testUnit")
class TestUnit : UnitCycle {
    override fun enable() {
        println("Test unit enable")
    }

    override fun disable() {
        println("Test unit  ")
    }
}
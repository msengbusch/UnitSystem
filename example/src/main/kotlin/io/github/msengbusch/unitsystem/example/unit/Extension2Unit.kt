package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.unit.Unit

@Unit("extension2", inheritable = true)
interface Extension2Unit {
    fun enable()
    fun disable()
}
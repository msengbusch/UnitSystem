package io.github.msengbusch.unitsystem.example.event

import io.github.msengbusch.unitsystem.event.Event

@Event
interface UnitReload {
    fun reload()
}
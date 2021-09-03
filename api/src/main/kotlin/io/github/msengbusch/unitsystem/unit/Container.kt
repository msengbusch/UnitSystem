package io.github.msengbusch.unitsystem.unit

import io.github.msengbusch.unitsystem.event.EventContainer

interface Container {
    val name: String
    val clazz: Class<*>
    val events: List<EventContainer>?
}
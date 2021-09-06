package io.github.msengbusch.unitsystem.unit

import io.github.msengbusch.unitsystem.event.EventContainer

interface Container<T> {
    val name: String
    val clazz: Class<T>
    val events: List<EventContainer>?
    val before: List<Container<*>>
    val after: List<Container<*>>
}
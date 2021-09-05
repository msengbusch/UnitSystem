package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.event.EventContainer
import io.github.msengbusch.unitsystem.unit.Container
import io.github.msengbusch.unitsystem.unit.Instance

interface Context {
    val events: Map<Class<*>, EventContainer>
    val units: Map<Class<*>, Container<*>>
    val instances: Map<Container<*>, List<Instance<*>>>

    val eventsToUnits: Map<EventContainer, List<Container<*>>>

    fun addEvent(event: EventContainer)
    fun addUnit(unit: Container<*>)
    fun addInstance(instance: Instance<*>)
}
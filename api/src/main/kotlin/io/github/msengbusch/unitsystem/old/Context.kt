package io.github.msengbusch.unitsystem.old

import io.github.msengbusch.unitsystem.old.event.EventContainer
import io.github.msengbusch.unitsystem.old.unit.Container
import io.github.msengbusch.unitsystem.old.unit.Instance

interface Context {
    val events: Map<Class<*>, EventContainer>
    val units: Map<Class<*>, Container<*>>
    val instances: Map<Container<*>, List<Instance<*>>>

    val eventsToUnits: Map<EventContainer, List<Container<*>>>

    fun addEvent(event: EventContainer)
    fun addUnit(unit: Container<*>)
    fun addInstance(instance: Instance<*>)
}
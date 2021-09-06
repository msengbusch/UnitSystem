package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.event.EventContainer
import io.github.msengbusch.unitsystem.unit.Container
import io.github.msengbusch.unitsystem.unit.Instance

class DefaultContext : Context {
    override val events: MutableMap<Class<*>, EventContainer> = mutableMapOf()
    override val units: MutableMap<Class<*>, Container<*>> = linkedMapOf()
    override val instances: MutableMap<Container<*>, MutableList<Instance<*>>> = linkedMapOf()
    override val eventsToUnits: MutableMap<EventContainer, MutableList<Container<*>>> = mutableMapOf()

    override fun addEvent(event: EventContainer) {
        events[event.clazz] = event
    }

    override fun addUnit(unit: Container<*>) {
        units[unit.clazz] = unit

        unit.events?.forEach { event ->
            val foundEvents = eventsToUnits[event]
            if (foundEvents == null) {
                eventsToUnits[event] = mutableListOf(unit)
            } else {
                foundEvents.add(unit)
            }
        }
    }

    override fun addInstance(instance: Instance<*>) {
        val foundInstances = instances[instance.container]
        if (foundInstances == null) {
            instances[instance.container] = mutableListOf(instance)
        } else {
            foundInstances.add(instance)
        }
    }
}
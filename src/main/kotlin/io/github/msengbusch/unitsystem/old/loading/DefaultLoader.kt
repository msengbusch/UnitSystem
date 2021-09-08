package io.github.msengbusch.unitsystem.old.loading

import io.github.msengbusch.unitsystem.DefaultContext
import io.github.msengbusch.unitsystem.exception.UnitException
import io.github.msengbusch.unitsystem.old.Context
import io.github.msengbusch.unitsystem.old.event.DefaultEventContainer
import io.github.msengbusch.unitsystem.old.event.EventContainer
import io.github.msengbusch.unitsystem.old.unit.Container
import io.github.msengbusch.unitsystem.old.unit.DefaultContainer

class DefaultLoader : Loader {
    override fun loadContext(parser: Parser): Context {
        val events = loadEvents(parser)
        val units = loadUnits(parser, events)

        val context = DefaultContext()

        events.values.forEach { event -> context.addEvent(event) }
        units.forEach { unit -> context.addUnit(unit) }

        return context
    }

    private fun loadEvents(parser: Parser): Map<String, EventContainer> {
        val events = mutableMapOf<String, EventContainer>()
        parser.events.forEach { entry ->
            val clazz: Class<*>

            try {
                clazz = this::class.java.classLoader.loadClass(entry.className)
            } catch (e: ClassNotFoundException) {
                throw UnitException("Could not find class for $entry")
            }

            events[entry.className!!] = DefaultEventContainer(clazz)
        }

        return events
    }

    private fun loadUnits(parser: Parser, events: Map<String, EventContainer>): List<Container<*>> {
        val units = mutableListOf<Container<*>>()

        parser.units.forEach { entry ->
            val clazz: Class<*>
            try {
                clazz = this::class.java.classLoader.loadClass(entry.className)
            } catch (e: ClassNotFoundException) {
                throw UnitException("Could not find class for $entry")
            }

            val eventContainers = entry.events?.map { event ->
                if (!events.containsKey(event)) {
                    throw UnitException("$entry requests unknown UnitEvent: $event")
                }

                events[event]!!
            }

            units.add(DefaultContainer(entry.name!!, clazz, eventContainers) as Container<*>)
        }

        return units
    }
}
package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.Context
import io.github.msengbusch.unitsystem.DefaultContext
import io.github.msengbusch.unitsystem.event.DefaultEventContainer
import io.github.msengbusch.unitsystem.event.EventContainer
import io.github.msengbusch.unitsystem.exception.LoadingException
import io.github.msengbusch.unitsystem.unit.Container
import io.github.msengbusch.unitsystem.unit.DefaultContainer

class DefaultLoader : Loader {
    override fun loadContext(parser: Parser): Context {
        val events = loadEvents(parser)
        val units = loadUnits(parser, events)

        return DefaultContext(events, units)
    }

    private fun loadEvents(parser: Parser): Map<Class<*>, EventContainer> {
        val events = mutableMapOf<Class<*>, EventContainer>()
        parser.events.forEach { entry ->
            val clazz: Class<*>

            try {
                clazz = this::class.java.classLoader.loadClass(entry.className)
            } catch (e: ClassNotFoundException) {
                throw LoadingException("Could not find class for $entry")
            }

            events[clazz] = DefaultEventContainer(clazz)
        }

        return events
    }

    private fun loadUnits(parser: Parser, events: Map<Class<*>, EventContainer>): Map<Class<*>, Container<*>> {
        val eventsByClassName = events.mapKeys { (clazz, _) -> clazz.name }
        val units = mutableMapOf<Class<*>, Container<*>>()

        parser.units.forEach { entry ->
            val clazz: Class<*>
            try {
                clazz = this::class.java.classLoader.loadClass(entry.className)
            } catch (e: ClassNotFoundException) {
                throw LoadingException("Could not find class for $entry")
            }

            val eventContainers = entry.events?.map { event ->
                if (!eventsByClassName.containsKey(event)) {
                    throw LoadingException("$entry requests unknown UnitEvent: $event")
                }

                eventsByClassName[event]!!
            }

            units[clazz] = DefaultContainer(entry.name!!, clazz, eventContainers) as Container<*>
        }

        return units
    }
}
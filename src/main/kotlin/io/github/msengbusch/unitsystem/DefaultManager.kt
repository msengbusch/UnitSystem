package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.exception.UnitException
import java.util.function.Consumer

class DefaultManager(private val context: Context) : Manager {

    @Suppress("UNCHECKED_CAST")
    override fun <T> callEvent(clazz: Class<T>, function: Consumer<T>) {
        val event = context.events[clazz] ?: throw UnitException("Unknown unit event class: ${clazz.name}")
        val units = context.eventsToUnits[event] ?: return

        units.forEach { unit ->
            context.instances[unit]?.forEach { instance ->
                function.accept(instance.instance as T)
            }
        }
    }
}
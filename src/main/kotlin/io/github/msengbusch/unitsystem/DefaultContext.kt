package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.event.EventContainer
import io.github.msengbusch.unitsystem.unit.Container

class DefaultContext(
    override val events: Map<Class<*>, EventContainer>,
    override val units: Map<Class<*>, Container<*>>
) :
    Context
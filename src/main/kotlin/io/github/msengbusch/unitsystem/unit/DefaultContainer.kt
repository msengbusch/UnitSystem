package io.github.msengbusch.unitsystem.unit

import io.github.msengbusch.unitsystem.event.EventContainer

class DefaultContainer<T>(
    override val name: String,
    override val clazz: Class<T>,
    override val events: List<EventContainer>?
) : Container<T>
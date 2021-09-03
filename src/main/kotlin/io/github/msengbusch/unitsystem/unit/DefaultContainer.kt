package io.github.msengbusch.unitsystem.unit

import io.github.msengbusch.unitsystem.event.EventContainer

class DefaultContainer(
    override val name: String,
    override val clazz: Class<*>,
    override val events: List<EventContainer>?
) : Container
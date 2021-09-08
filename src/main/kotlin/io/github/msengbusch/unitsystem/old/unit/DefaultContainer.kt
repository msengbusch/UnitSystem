package io.github.msengbusch.unitsystem.old.unit

import io.github.msengbusch.unitsystem.old.event.EventContainer

data class DefaultContainer<T>(
    override val name: String,
    override val clazz: Class<T>,
    override val events: List<EventContainer>?
) : Container<T> {

    override lateinit var before: List<Container<*>>
        internal set
    override lateinit var after: List<Container<*>>
        internal set
}
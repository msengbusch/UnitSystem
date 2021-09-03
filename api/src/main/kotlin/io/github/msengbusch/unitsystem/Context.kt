package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.event.EventContainer
import io.github.msengbusch.unitsystem.unit.Container

interface Context {
    val events: Map<Class<*>, EventContainer>
    val units: Map<Class<*>, Container>
}
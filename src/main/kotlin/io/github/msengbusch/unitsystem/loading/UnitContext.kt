package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.event.UnitEventContainer
import io.github.msengbusch.unitsystem.unit.UnitContainer

class UnitContext(val unitEvents: Map<Class<*>, UnitEventContainer>, val units: Map<Class<*>, UnitContainer>)
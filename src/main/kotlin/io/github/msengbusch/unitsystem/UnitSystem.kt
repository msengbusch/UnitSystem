package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.loading.UnitLoader

class UnitSystem(private val loader: UnitLoader) {
    val context = loader.loadContext()


}
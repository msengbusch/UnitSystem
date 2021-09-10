package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.unit.Unit

@Unit("extension", inheritable = true)
interface ExtensionUnit {
    val name: String
}
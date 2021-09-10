package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.unit.Unit
import javax.inject.Inject

@Unit("module")
class ModuleUnit @Inject constructor(val component: ComponentUnit) : ExtensionUnit, Extension2Unit {
    override val name: String = "Module"

    override fun enable() {
        println("Enable module")
    }

    override fun disable() {
        println("Disable module")
    }
}
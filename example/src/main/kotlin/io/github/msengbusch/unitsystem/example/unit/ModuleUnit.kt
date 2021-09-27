package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.unit.Module
import javax.inject.Inject

@Module
class ModuleUnit @Inject constructor(val component: ComponentUnit) : ExtensionUnit, Extension2 {
    override val name: String = "Module"

    override fun enable() {
        println("Enable module")
    }

    override fun disable() {
        println("Disable module")
    }
}
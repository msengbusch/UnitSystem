package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.scope.identifier.FromModule
import io.github.msengbusch.unitsystem.unit.Component
import io.github.msengbusch.unitsystem.unit.Container
import javax.inject.Inject

@Component
class ComponentUnit @Inject constructor(
    @FromModule private val container: Container<*>,
    private val extensionUnit: ExtensionUnit
) : Extension2 {
    override fun enable() {
        println("Enable component extension for ${extensionUnit.name}")
        println("Enable component for ${container.name}")
    }

    override fun disable() {
        println("Disable component extension for ${extensionUnit.name}")
        println("Disable component for ${container.name}")
    }

    fun ejpwe() {

    }
}
package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.scope.identifier.FromInheriting
import io.github.msengbusch.unitsystem.unit.Container
import io.github.msengbusch.unitsystem.unit.Unit
import javax.inject.Inject

@Unit("component", unique = false)
class ComponentUnit @Inject constructor(
    @FromInheriting private val inheritingContainer: Container<*>,
    private val extensionUnit: ExtensionUnit
) : Extension2Unit {
    override fun enable() {
        println("Enable component extension for ${extensionUnit.name}")
        println("Enable component for ${inheritingContainer.name}")
    }

    override fun disable() {
        println("Disable component extension for ${extensionUnit.name}")
        println("Disable component for ${inheritingContainer.name}")
    }
}
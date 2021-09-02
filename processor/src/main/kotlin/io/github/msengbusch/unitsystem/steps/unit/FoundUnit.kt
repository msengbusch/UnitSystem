package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.steps.event.FoundUnitEvent

data class FoundUnit(val name: String, val clazz: String, val parentClasses: List<String>) {
    var unitEvents: MutableList<FoundUnitEvent>? = null

    override fun toString(): String {
        var string = "FoundUnit(name='$name', clazz='$clazz'"

        string = if (unitEvents == null) {
            "$string, parentClasses=$parentClasses"
        } else {
            "$string, unitEvents=$unitEvents"
        }

        return string
    }
}

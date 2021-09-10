package io.github.msengbusch.unitsystem.unit

import io.github.msengbusch.unitsystem.util.ClassName
import io.github.msengbusch.unitsystem.util.Name

data class ValidUnit(
    val preUnit: PreUnit, val before: List<PreUnit>, val after: List<PreUnit>, val inherited: List<PreUnit>,
    val components: List<PreUnit>
) {
    val name: Name = preUnit.name
    val className: ClassName = preUnit.className

    val isComponent: Boolean = preUnit.isComponent
    val isInheritable: Boolean = preUnit.isInheritable
    val isInstanciable: Boolean = preUnit.isInstanciable
}
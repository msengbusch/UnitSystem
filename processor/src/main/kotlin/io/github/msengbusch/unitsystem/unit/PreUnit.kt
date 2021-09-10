package io.github.msengbusch.unitsystem.unit

import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import io.github.msengbusch.unitsystem.util.ClassName
import io.github.msengbusch.unitsystem.util.Name

data class PreUnit(
    val name: Name, val className: ClassName, val before: List<Name>, val after: List<Name>,
    val parentClasses: List<KSTypeReference>, val constructorParams: List<KSValueParameter>, val isUnique: Boolean,
    val isInheritable: Boolean, val isInstanciable: Boolean
)
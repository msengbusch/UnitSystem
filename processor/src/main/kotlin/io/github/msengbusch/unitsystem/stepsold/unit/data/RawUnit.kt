package io.github.msengbusch.unitsystem.stepsold.unit.data

typealias UnitName = String

data class RawUnit(
    val name: String,
    val className: String,
    val before: List<UnitName>,
    val after: List<UnitName>,
    val parentClasses: List<String>
)
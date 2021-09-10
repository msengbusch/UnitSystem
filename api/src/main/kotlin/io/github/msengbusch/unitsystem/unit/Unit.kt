package io.github.msengbusch.unitsystem.unit

annotation class Unit(
    val name: String,
    val component: Boolean = false,
    val inheritable: Boolean = false,
    val before: Array<String> = [],
    val after: Array<String> = []
)

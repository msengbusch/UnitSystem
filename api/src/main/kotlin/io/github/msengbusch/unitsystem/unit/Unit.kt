package io.github.msengbusch.unitsystem.unit


// Before: This unit is executed before these units
// After: This unit is executed after these units
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Unit(val name: String, val before: Array<String> = [], val after: Array<String> = [])

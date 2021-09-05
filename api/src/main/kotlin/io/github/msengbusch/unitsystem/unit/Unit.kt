package io.github.msengbusch.unitsystem.unit

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Unit(val name: String, val before: Array<String>, val after: Array<String>)

package io.github.msengbusch.unitsystem.unit

interface Container<T> {
    val name: String
    val clazz: Class<T>

    val unique: Boolean
    val inheritable: Boolean

    // Units executed ... this unit
    val before: List<Container<*>>
    val after: List<Container<*>>

    val injection: List<Container<T>>
    val uniqueInjection: List<Container<T>>
    val inherits: List<Container<T>>
}
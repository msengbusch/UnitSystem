package io.github.msengbusch.unitsystem.unit

interface Instance<T> {
    val container: Container<T>

    val instance: T
}
package io.github.msengbusch.unitsystem.old.unit

interface Instance<T> {
    val container: Container<T>

    val instance: T
}
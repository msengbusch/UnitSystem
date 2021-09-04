package io.github.msengbusch.unitsystem.unit

class DefaultInstance<T>(override val container: Container<T>, override val instance: T) : Instance<T>
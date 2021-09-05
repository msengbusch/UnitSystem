package io.github.msengbusch.unitsystem.exception

class UnitException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message: String) : this(message, null)
}
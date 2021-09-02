package io.github.msengbusch.unitsystem.exception

class InjectionException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message: String) : this(message, null)
}
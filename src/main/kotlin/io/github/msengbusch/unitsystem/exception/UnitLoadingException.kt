package io.github.msengbusch.unitsystem.exception

class UnitLoadingException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message: String) : this(message, null)
}
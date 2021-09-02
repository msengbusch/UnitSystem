package io.github.msengbusch.unitsystem.exception

class UnitLoadingException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
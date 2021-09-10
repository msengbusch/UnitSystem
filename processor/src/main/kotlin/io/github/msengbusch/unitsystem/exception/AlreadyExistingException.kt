package io.github.msengbusch.unitsystem.exception

class AlreadyExistingException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
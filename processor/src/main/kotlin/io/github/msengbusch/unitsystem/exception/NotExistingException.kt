package io.github.msengbusch.unitsystem.exception

class NotExistingException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
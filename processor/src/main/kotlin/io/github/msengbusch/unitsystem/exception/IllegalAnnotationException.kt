package io.github.msengbusch.unitsystem.exception

class IllegalAnnotationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
package io.github.msengbusch.unitsystem.exception

class MissingAnnotationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
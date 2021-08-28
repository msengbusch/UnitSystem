package io.github.msengbusch.unitsystem.util

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

fun ProcessingEnvironment.fatal(message: String) {
    error("FATAL: $message")
}

fun ProcessingEnvironment.error(message: String) {
    messager.printMessage(Diagnostic.Kind.ERROR, message)
}

fun ProcessingEnvironment.warn(message: String) {
    messager.printMessage(Diagnostic.Kind.WARNING, message)
}

fun ProcessingEnvironment.info(message: String) {
    messager.printMessage(Diagnostic.Kind.NOTE, message)
}

fun ProcessingEnvironment.debug(message: String) {
    messager.printMessage(Diagnostic.Kind.OTHER, message)
}
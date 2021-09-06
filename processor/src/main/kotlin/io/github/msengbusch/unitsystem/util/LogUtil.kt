package io.github.msengbusch.unitsystem.util

import io.github.msengbusch.unitsystem.context.Context
import javax.tools.Diagnostic

fun Context.fatal(message: String) {
    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "FATAL: $message")
}

fun Context.error(message: String) {
    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
}

fun Context.warn(message: String) {
    processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, message)
}

fun Context.info(message: String) {
    processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, message)
}

fun Context.debug(message: String) {
    processingEnv.messager.printMessage(Diagnostic.Kind.OTHER, message)
}
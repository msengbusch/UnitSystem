package io.github.msengbusch.unitsystem.util

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
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
    messager.printMessage(Diagnostic.Kind.NOTE, message)
}

fun ProcessingEnvironment.fatal(message: String, e: Element) {
    error("FATAL: $message", e)
}

fun ProcessingEnvironment.error(message: String, e: Element) {
    messager.printMessage(Diagnostic.Kind.ERROR, message, e)
}

fun ProcessingEnvironment.warn(message: String, e: Element) {
    messager.printMessage(Diagnostic.Kind.WARNING, message, e)
}

fun ProcessingEnvironment.info(message: String, e: Element) {
    messager.printMessage(Diagnostic.Kind.NOTE, message, e)
}

fun ProcessingEnvironment.debug(message: String, e: Element) {
    messager.printMessage(Diagnostic.Kind.NOTE, message, e)
}

fun ProcessingEnvironment.fatal(message: String, e: Element, a: AnnotationMirror) {
    error("FATAL: $message", e, a)
}

fun ProcessingEnvironment.error(message: String, e: Element, a: AnnotationMirror) {
    messager.printMessage(Diagnostic.Kind.ERROR, message, e, a)
}

fun ProcessingEnvironment.warn(message: String, e: Element, a: AnnotationMirror) {
    messager.printMessage(Diagnostic.Kind.WARNING, message, e, a)
}

fun ProcessingEnvironment.info(message: String, e: Element, a: AnnotationMirror) {
    messager.printMessage(Diagnostic.Kind.NOTE, message, e, a)
}

fun ProcessingEnvironment.debug(message: String, e: Element, a: AnnotationMirror) {
    messager.printMessage(Diagnostic.Kind.NOTE, message, e, a)
}

fun ProcessingEnvironment.fatal(message: String, e: Element, a: AnnotationMirror, aV: AnnotationValue) {
    error("FATAL: $message", e, a, aV)
}

fun ProcessingEnvironment.error(message: String, e: Element, a: AnnotationMirror, aV: AnnotationValue) {
    messager.printMessage(Diagnostic.Kind.ERROR, message, e, a, aV)
}

fun ProcessingEnvironment.warn(message: String, e: Element, a: AnnotationMirror, aV: AnnotationValue) {
    messager.printMessage(Diagnostic.Kind.WARNING, message, e, a, aV)
}

fun ProcessingEnvironment.info(message: String, e: Element, a: AnnotationMirror, aV: AnnotationValue) {
    messager.printMessage(Diagnostic.Kind.NOTE, message, e, a, aV)
}

fun ProcessingEnvironment.debug(message: String, e: Element, a: AnnotationMirror, aV: AnnotationValue) {
    messager.printMessage(Diagnostic.Kind.NOTE, message, e, a, aV)
}
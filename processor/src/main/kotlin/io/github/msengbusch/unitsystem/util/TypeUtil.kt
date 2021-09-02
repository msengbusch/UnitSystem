package io.github.msengbusch.unitsystem.util

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

fun TypeMirror.asElement(): Element {
    return (this as DeclaredType).asElement()
}

fun TypeMirror.asTypeElement(): TypeElement {
    return asElement() as TypeElement
}
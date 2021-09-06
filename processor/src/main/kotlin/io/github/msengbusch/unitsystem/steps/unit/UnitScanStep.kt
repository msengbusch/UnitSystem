package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.context.Context
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.steps.unit.data.RawUnit
import io.github.msengbusch.unitsystem.steps.unit.data.UnitName
import io.github.msengbusch.unitsystem.unit.Unit
import io.github.msengbusch.unitsystem.util.asTypeElement
import io.github.msengbusch.unitsystem.util.debug
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class UnitScanStep : AnnotationStep<Unit> {
    override val supportedAnnotation: Class<Unit> = Unit::class.java
    override val supportedElementKinds: List<ElementKind> = listOf(ElementKind.INTERFACE, ElementKind.CLASS)

    val units = mutableMapOf<UnitName, RawUnit>()

    override fun process(context: Context, element: Element, annotation: Unit) {
        val type = element as TypeElement

        val className = type.qualifiedName.toString()
        val name = annotation.name

        if(units.containsKey(name)) {
            throw IllegalArgumentException("Unit $className of name $name is already registered as ${units[name]!!.className}")
        }

        val before = annotation.before.toList()
        val after = annotation.after.toList()

        val parentClassNames = mutableListOf<String>()

        val superClassType = type.superclass?.asTypeElement()
        val superClassName = superClassType?.qualifiedName.toString()
        if(superClassType != null && superClassName != "java.lang.Object") {
            parentClassNames.add(superClassName)
        }

        parentClassNames.addAll(type.interfaces.map { it.asTypeElement().qualifiedName.toString() })

        val unit = RawUnit(name, className, before, after, parentClassNames)
        units[name] = unit

        context.debug("Scanned $unit")
    }
}
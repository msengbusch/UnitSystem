package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.context.ScanContext
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.steps.event.ScanEvent
import io.github.msengbusch.unitsystem.unit.Unit
import io.github.msengbusch.unitsystem.util.asTypeElement
import io.github.msengbusch.unitsystem.util.debug
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class UnitStep : AnnotationStep<Unit> {
    override val annotationClazz: Class<Unit> = Unit::class.java
    override val allowedElementKinds: Set<ElementKind> = setOf(ElementKind.CLASS, ElementKind.INTERFACE)

    override fun process(
        processingEnv: ProcessingEnvironment,
        scanContext: ScanContext,
        processContext: ProcessContext
    ) {
        scanContext.units.forEach { (_, unit) ->
            val events = mutableListOf<ScanEvent>()

            unit.parentClasses.forEach parentClass@{ parentClass ->
                if (!scanContext.events.containsKey(parentClass)) {
                    processingEnv.debug("Parent class $parentClass of $unit ignoring it as event because it isn't annotated with @UnitEvent")
                    return@parentClass
                } else {
                    events.add(scanContext.events[parentClass]!!)
                }
            }

            processingEnv.debug("Processed Unit $unit")
        }
    }

    override fun output(
        processingEnv: ProcessingEnvironment,
        scanContext: ScanContext,
        processContext: ProcessContext,
        outputContext: OutputContext
    ) {
        val lines = mutableListOf<String>()

        processContext.units.forEach { (_, unit) ->
            lines.add("[Unit]")
            lines.add("name=${unit.name}")
            lines.add("class=${unit.className}")

            if (unit.events.isNotEmpty()) {
                lines.add("events=${unit.events.joinToString(",") { it.className }}")
            }
        }
    }

    override fun scan(
        element: Element,
        annotation: Unit,
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        scanContext: ScanContext
    ) {
        val type = element as TypeElement

        val className = type.qualifiedName.toString()
        val name = annotation.name

        val parentClassNames = mutableListOf<String>()

        val superClassType = type.superclass?.asTypeElement()
        val superClassName = superClassType?.qualifiedName.toString()

        if (superClassType != null && superClassName != "java.lang.Object") {
            parentClassNames.add(superClassName)
        }

        parentClassNames.addAll(type.interfaces.map { it.asTypeElement().qualifiedName.toString() })

        val unit = ScanUnit(name, className, parentClassNames)
        scanContext.units[className] = unit

        processingEnv.debug("Scanned $unit")
    }
}
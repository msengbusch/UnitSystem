package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.unit.Unit
import io.github.msengbusch.unitsystem.util.asTypeElement
import io.github.msengbusch.unitsystem.util.debug
import io.github.msengbusch.unitsystem.util.info
import io.github.msengbusch.unitsystem.util.writeToResource
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class UnitStep : AnnotationStep<Unit> {
    override val annotationClazz: Class<Unit> = Unit::class.java
    override val allowedElementKinds: Set<ElementKind> = setOf(ElementKind.CLASS, ElementKind.INTERFACE)

    override fun process(processingEnv: ProcessingEnvironment, processContext: ProcessContext) {
        processContext.units.forEach { (_, unit) ->
            process(unit, processingEnv, processContext)
        }
    }

    private fun process(unit: FoundUnit, processingEnv: ProcessingEnvironment, processContext: ProcessContext) {
        unit.unitEvents = mutableListOf()

        unit.parentClasses.forEach { parentClassName ->
            if (processContext.unitEvents.containsKey(parentClassName)) {
                unit.unitEvents!!.add(processContext.unitEvents[parentClassName]!!)
            }
        }

        processingEnv.info("Processed @Unit $unit")
    }

    override fun output(
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext,
        outputContext: OutputContext
    ) {
        val lines = outputContext.unitsFile

        processContext.units.forEach { (_, unit) ->
            lines.add("[Unit]")
            lines.add("name=${unit.name}")
            lines.add("class=${unit.clazz}")
            lines.add("events=${unit.unitEvents!!.joinToString(",") { it.clazz }}")
        }

        processingEnv.writeToResource("units", lines.joinToString("\n"))
    }

    override fun scan(
        element: Element,
        annotation: Unit,
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext
    ) {
        val type = element as TypeElement

        val clazzName = type.qualifiedName.toString()
        val name = annotation.name

        val parentClassNames = mutableListOf<String>()
        val superClass = type.superclass?.asTypeElement()
        val superClassName = superClass?.qualifiedName.toString()
        if (superClass != null && superClassName != "java.lang.Object") {
            parentClassNames.add(superClassName)
        }

        parentClassNames.addAll(type.interfaces.map { it.asTypeElement().qualifiedName.toString() })

        val unit = FoundUnit(name, clazzName, parentClassNames)
        processContext.units[clazzName] = unit

        processingEnv.debug("Found @Unit $unit")
    }
}
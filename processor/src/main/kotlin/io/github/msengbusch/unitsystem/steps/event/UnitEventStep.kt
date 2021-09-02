package io.github.msengbusch.unitsystem.steps.event

import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.event.UnitEvent
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.util.info
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class UnitEventStep : AnnotationStep<UnitEvent> {
    override val annotationClazz: Class<UnitEvent> = UnitEvent::class.java
    override val allowedElementKinds: Set<ElementKind> = setOf(ElementKind.CLASS, ElementKind.INTERFACE)

    override fun process(processingEnv: ProcessingEnvironment, processContext: ProcessContext) {

    }

    override fun output(
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext,
        outputContext: OutputContext
    ) {
        val lines = mutableListOf<String>()

        processContext.unitEvents.forEach { (_, unitEvent) ->
            lines.add("[UnitEvent]")
            lines.add("class=${unitEvent.clazz}")
        }

        outputContext.unitsFile.addAll(lines)
    }


    override fun scan(
        element: Element,
        annotation: UnitEvent,
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext
    ) {
        val type = element as TypeElement
        val clazzName = type.qualifiedName.toString()

        val unitEvent = FoundUnitEvent(clazzName)
        processContext.unitEvents[clazzName] = unitEvent

        processingEnv.info("Found @UnitEvent $unitEvent")
    }
}
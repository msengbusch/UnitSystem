package io.github.msengbusch.unitsystem.steps.event

import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.context.ScanContext
import io.github.msengbusch.unitsystem.event.Event
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.util.info
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class EventStep : AnnotationStep<Event> {
    override val annotationClazz: Class<Event> = Event::class.java
    override val allowedElementKinds: Set<ElementKind> = setOf(ElementKind.CLASS, ElementKind.INTERFACE)

    override fun process(
        processingEnv: ProcessingEnvironment,
        scanContext: ScanContext,
        processContext: ProcessContext
    ) {
        // Nothing to do
    }

    override fun output(
        processingEnv: ProcessingEnvironment,
        scanContext: ScanContext,
        processContext: ProcessContext,
        outputContext: OutputContext
    ) {
        val lines = mutableListOf<String>()

        scanContext.events.forEach { (_, event) ->
            lines.add("[Event]")
            lines.add("class=${event.className}")
        }

        outputContext.unitsFile.addAll(lines)
    }

    override fun scan(
        element: Element,
        annotation: Event,
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        scanContext: ScanContext
    ) {
        val type = element as TypeElement
        val className = type.qualifiedName.toString()

        val event = ScanEvent(className)
        scanContext.events[className] = event

        processingEnv.info("Scanned $event")
    }
}
package io.github.msengbusch.unitsystem.steps.event

import io.github.msengbusch.unitsystem.context.Context
import io.github.msengbusch.unitsystem.event.Event
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.steps.event.data.RawEvent
import io.github.msengbusch.unitsystem.util.debug
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class EventScanStep : AnnotationStep<Event> {
    override val supportedAnnotation: Class<Event> = Event::class.java
    override val supportedElementKinds: List<ElementKind> = listOf(ElementKind.CLASS, ElementKind.INTERFACE)

    val events = mutableMapOf<String, RawEvent>()

    override fun process(context: Context, element: Element, annotation: Event) {
        val type = element as TypeElement

        val className = type.qualifiedName.toString()

        val event = RawEvent(className)
        events[className] = event

        context.debug("Scanned $event")
    }
}
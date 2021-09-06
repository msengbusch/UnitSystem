package io.github.msengbusch.unitsystem.step

import io.github.msengbusch.unitsystem.context.Context
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

interface AnnotationStep<A> : Step where A : Annotation {
    val supportedAnnotation: Class<A>
    val supportedElementKinds: List<ElementKind>?

    override fun process(context: Context) {
        val elements = context.roundEnv.getElementsAnnotatedWith(supportedAnnotation)

        supportedElementKinds?.let { kinds ->
            elements.forEach { element ->
                if(!kinds.contains(element.kind)) {
                    throw IllegalArgumentException("${supportedAnnotation.name} was used on ${element.simpleName} of kind ${element.kind} but only those kinds are allowed: ${kinds.joinToString()}")
                }
            }
        }

        elements.forEach { element ->
            process(context, element, element.getAnnotation(supportedAnnotation))
        }
    }

    fun process(context: Context, element: Element, annotation: A)
}
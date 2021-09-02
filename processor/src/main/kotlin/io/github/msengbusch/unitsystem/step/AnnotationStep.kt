package io.github.msengbusch.unitsystem.step

import io.github.msengbusch.unitsystem.context.ProcessContext
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

interface AnnotationStep<A> : Step where A : Annotation {
    val annotationClazz: Class<A>
    val allowedElementKinds: Set<ElementKind>?

    fun scan(
        element: Element,
        annotation: A,
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext
    )

    fun scan(
        elements: Map<out Element, A>,
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext
    ) {
        allowedElementKinds?.let { kinds ->
            elements.forEach { (element, _) ->
                if (!kinds.contains(element.kind)) {
                    throw IllegalArgumentException(
                        "Annotation ${annotationClazz.name} was used on ${element.simpleName} of kind ${element.kind} but only those kinds are allowed: ${
                            kinds.joinToString(
                                ","
                            )
                        }"
                    )
                }
            }
        }

        elements.forEach { (element, annotation) ->
            scan(element, annotation, roundEnv, processingEnv, processContext)
        }
    }

    override fun scan(
        roundEnv: RoundEnvironment,
        processingEnv: ProcessingEnvironment,
        processContext: ProcessContext
    ) {
        val elements = mutableMapOf<Element, A>()

        roundEnv.getElementsAnnotatedWith(annotationClazz).forEach { element ->
            elements[element] = element.getAnnotation(annotationClazz)
        }

        scan(elements, roundEnv, processingEnv, processContext)
    }
}
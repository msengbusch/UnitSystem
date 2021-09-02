package io.github.msengbusch.unitsystem.step

import io.github.msengbusch.unitsystem.context.ScanContext
import io.github.msengbusch.unitsystem.util.debug
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

interface AnnotationStep<A> : Step where A : Annotation {
    val annotationClazz: Class<A>
    val allowedElementKinds: Set<ElementKind>?

    fun scan(element: Element, annotation: A, roundEnv: RoundEnvironment, processingEnv: ProcessingEnvironment, scanContext: ScanContext)

    fun scan(elements: Map<out Element, A>, roundEnv: RoundEnvironment, processingEnv: ProcessingEnvironment, scanContext: ScanContext) {
        allowedElementKinds?.let { kinds ->
            elements.forEach { (element, _) ->
                if(!kinds.contains(element.kind)) {
                    throw IllegalArgumentException("Annotation ${annotationClazz.name} was used on ${element.simpleName} of kind ${element.kind} but only those kinds are allowed: ${kinds.joinToString(",")}")
                }
            }
        }

        elements.forEach { (element, annotation) ->
            scan(element, annotation, roundEnv, processingEnv, scanContext)
        }
    }

    override fun scan(roundEnv: RoundEnvironment, processingEnv: ProcessingEnvironment, scanContext: ScanContext) {
        val elements = mutableMapOf<Element, A>()

        roundEnv.getElementsAnnotatedWith(annotationClazz).forEach { element ->
            elements[element] = element.getAnnotation(annotationClazz)
        }

        scan(elements, roundEnv, processingEnv, scanContext)
    }
}
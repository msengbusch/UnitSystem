package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.Unit
import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.context.ScanContext
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.util.writeToResource
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.StandardLocation

class UnitStep : AnnotationStep<Unit> {
    override val annotationClazz: Class<Unit> = Unit::class.java
    override val allowedElementKinds: Set<ElementKind> = setOf(ElementKind.CLASS, ElementKind.INTERFACE)

    override fun process(processingEnv: ProcessingEnvironment, scanContext: ScanContext, processContext: ProcessContext) {

    }

    override fun output(processingEnv: ProcessingEnvironment, scanContext: ScanContext, processContext: ProcessContext, outputContext: OutputContext) {
        processingEnv.writeToResource("units", outputContext.unitsFile.joinToString("\n"))
    }

    override fun scan(element: Element, annotation: Unit, roundEnv: RoundEnvironment, processingEnv: ProcessingEnvironment, scanContext: ScanContext) {

    }
}
package io.github.msengbusch.unitsystem.processor

import io.github.msengbusch.unitsystem.Unit
import io.github.msengbusch.unitsystem.declaration.UnitDeclaration
import io.github.msengbusch.unitsystem.util.error
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind

object UnitProcessor {
    fun processUnits(processingEnv: ProcessingEnvironment, roundEnv: RoundEnvironment): List<UnitDeclaration> {
        val units = mutableListOf<UnitDeclaration>()

        roundEnv.getElementsAnnotatedWith(Unit::class.java).forEach { element ->
            if(element.kind != ElementKind.CLASS) {
                processingEnv.error("Only classes can be annotated with @Unit: $element")
            }

            val annotation = element.getAnnotation(Unit::class.java)
            val name = annotation.name

            val unit = UnitDeclaration(name)
            units.add(unit)
        }

        return units
    }
}
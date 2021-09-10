package io.github.msengbusch.unitsystem

import io.github.msengbusch.unitsystem.context.Context
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class AnnotationProcessor : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_16
    override fun getSupportedOptions(): MutableSet<String> = mutableSetOf(
        "org.gradle.annotation.processing.aggregating"
    )

    override fun getSupportedAnnotationTypes(): MutableSet<String> = context.getSupportedAnnotations()
        .map { clazz -> clazz.name }
        .toMutableSet()

    private val context = Context()

    init {
        //context.registerStepPerRound(UnitScanStep())
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        context.initialize(processingEnv)

        if(roundEnv.processingOver()) {
            context.finish()
        } else {
            context.round(roundEnv)
        }

        return false
    }
}
package io.github.msengbusch.unitsystem

import com.google.auto.service.AutoService
import io.github.msengbusch.unitsystem.context.Context
import io.github.msengbusch.unitsystem.steps.event.EventScanStep
import io.github.msengbusch.unitsystem.steps.unit.UnitOutputStep
import io.github.msengbusch.unitsystem.steps.unit.UnitScanStep
import io.github.msengbusch.unitsystem.steps.unit.UnitSortStep
import io.github.msengbusch.unitsystem.steps.unit.UnitValidateStep
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
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
        context.registerStepPerRound(EventScanStep())
        context.registerStepPerRound(UnitScanStep())

        context.registerStep(UnitValidateStep())
        context.registerStep(UnitSortStep())

        context.registerStep(UnitOutputStep())
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
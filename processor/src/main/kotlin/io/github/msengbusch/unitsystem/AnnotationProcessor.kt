package io.github.msengbusch.unitsystem

import com.google.auto.service.AutoService
import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.step.Step
import io.github.msengbusch.unitsystem.steps.event.UnitEventStep
import io.github.msengbusch.unitsystem.steps.unit.UnitStep
import io.github.msengbusch.unitsystem.util.fatal
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

    override fun getSupportedAnnotationTypes(): MutableSet<String> = steps
        .filterIsInstance<AnnotationStep<*>>()
        .map { it.annotationClazz.name }
        .toMutableSet()

    private val steps = listOf<Step>(
        UnitEventStep(),
        UnitStep()
    )

    private val processContext = ProcessContext()
    private val outputContext = OutputContext()

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        try {
            processImpl(roundEnv)
        } catch (e: Exception) {
            processingEnv.fatal("A fatal exception occurred")
            e.printStackTrace()
        }

        return false
    }

    private fun processImpl(roundEnv: RoundEnvironment) {
        if(!roundEnv.processingOver()) {
            scanSteps(roundEnv)
            processSteps()
        } else {
            outputSteps()
        }
    }

    private fun scanSteps(roundEnv: RoundEnvironment) {
        steps.forEach { step ->
            step.scan(roundEnv, processingEnv, processContext)
        }
    }

    private fun processSteps() {
        steps.forEach { step ->
            step.process(processingEnv, processContext)
        }
    }

    private fun outputSteps() {
        steps.forEach { step ->
            step.output(processingEnv, processContext, outputContext)
        }
    }
}
package io.github.msengbusch.unitsystem.context

import io.github.msengbusch.unitsystem.step.AnnotationStep
import io.github.msengbusch.unitsystem.step.Step
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class Context {
    lateinit var roundEnv: RoundEnvironment
        private set

    lateinit var processingEnv: ProcessingEnvironment
        private set

    private var initialized: Boolean = false

    private val steps = LinkedHashMap<Class<*>, Step>()
    private val perRoundSteps = LinkedHashMap<Class<*>, Step>()

    fun initialize(processingEnv: ProcessingEnvironment) {
        if(initialized) return
        this.initialized = true

        this.processingEnv = processingEnv
    }

    fun round(roundEnv: RoundEnvironment) {
        this.roundEnv = roundEnv

        perRoundSteps.forEach { (_, step) ->
            step.process(this)
        }
    }

    fun finish() {
        steps.forEach { (_, step) ->
            step.process(this)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getStep(clazz: Class<T>): T where T : Step {
        return if(steps.containsKey(clazz)) {
            steps[clazz] as T
        } else if(perRoundSteps.containsKey(clazz)) {
            perRoundSteps[clazz] as T
        } else {
            throw IllegalArgumentException("Step ${clazz.name} is unknown. Please register it")
        }
    }

    fun registerStepPerRound(step: Step) {
        if(steps.containsKey(step.javaClass)) {
            throw IllegalArgumentException("Step ${step.javaClass.name} is already registered as step at finish")
        }

        perRoundSteps[step.javaClass] = step
    }

    fun registerStep(step: Step) {
        if(perRoundSteps.containsKey(step.javaClass)) {
            throw IllegalArgumentException("Step ${step.javaClass.name} is already registered as step at finish")
        }

        steps[step.javaClass] = step
    }

    fun getSupportedAnnotations(): List<Class<*>> {
        val allSteps = mutableListOf<Class<*>>()

        allSteps.addAll(steps.values
            .filterIsInstance<AnnotationStep<*>>()
            .map { step -> step.supportedAnnotation })

        allSteps.addAll(perRoundSteps.values
            .filterIsInstance<AnnotationStep<*>>()
            .map { step -> step.supportedAnnotation })

        return allSteps
    }
}
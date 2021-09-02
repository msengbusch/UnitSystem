package io.github.msengbusch.unitsystem.step

import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

interface Step {
    fun scan(roundEnv: RoundEnvironment, processingEnv: ProcessingEnvironment, processContext: ProcessContext)
    fun process(processingEnv: ProcessingEnvironment, processContext: ProcessContext)
    fun output(processingEnv: ProcessingEnvironment, processContext: ProcessContext, outputContext: OutputContext)
}
package io.github.msengbusch.unitsystem.step

import io.github.msengbusch.unitsystem.context.OutputContext
import io.github.msengbusch.unitsystem.context.ProcessContext
import io.github.msengbusch.unitsystem.context.ScanContext
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

interface Step {
    fun scan(roundEnv: RoundEnvironment, processingEnv: ProcessingEnvironment, scanContext: ScanContext)
    fun process(processingEnv: ProcessingEnvironment, scanContext: ScanContext, processContext: ProcessContext)
    fun output(processingEnv: ProcessingEnvironment, scanContext: ScanContext, processContext: ProcessContext, outputContext: OutputContext)
}
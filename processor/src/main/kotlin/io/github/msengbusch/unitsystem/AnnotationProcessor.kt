package io.github.msengbusch.unitsystem

import com.google.auto.service.AutoService
import io.github.msengbusch.unitsystem.declaration.UnitDeclaration
import io.github.msengbusch.unitsystem.output.UnitFile
import io.github.msengbusch.unitsystem.processor.UnitProcessor
import io.github.msengbusch.unitsystem.util.fatal
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import kotlin.io.path.Path

@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {
    private val units = mutableListOf<UnitDeclaration>()

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(Unit::class.java.name)
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_16

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        try {
            processImpl(roundEnv)
        } catch (e: Exception) {
            processingEnv.fatal("FATAL: An exception occurred")
            e.printStackTrace()
        }

        return true
    }

    private fun processImpl(roundEnv: RoundEnvironment) {
        if(roundEnv.processingOver()) {
            generateOutput()
        } else {
            processAnnotations( roundEnv)
        }
    }

    private fun processAnnotations(roundEnv: RoundEnvironment) {
        units.addAll(UnitProcessor.processUnits(processingEnv, roundEnv))
    }

    private fun generateOutput() {
        val generatedSourcesRoot = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

        UnitFile.writeUnitFile(Path(generatedSourcesRoot, UNIT_FILE_RESOURCE), units)
    }

    companion object {
        const val UNIT_FILE_RESOURCE = "units"
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}
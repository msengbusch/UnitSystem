package io.github.msengbusch.unitsystem

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.msengbusch.unitsystem.extension.ExtensionOutput
import io.github.msengbusch.unitsystem.extension.ExtensionScan
import io.github.msengbusch.unitsystem.extension.PreExtension

class Processor(env: SymbolProcessorEnvironment) : SymbolProcessor {
    private val logger = env.logger
    private val generator = env.codeGenerator

    private val scannedExtensions = mutableMapOf<String, PreExtension>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        scannedExtensions.putAll(ExtensionScan.scanExtensions(resolver, logger))

        return emptyList()
    }

    override fun finish() {
        ExtensionOutput.generateOutput(scannedExtensions.map { it.value }, generator, logger)
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment)
    }
}
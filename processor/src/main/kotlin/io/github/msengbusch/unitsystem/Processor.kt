package io.github.msengbusch.unitsystem

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.msengbusch.unitsystem.component.ComponentOutput
import io.github.msengbusch.unitsystem.component.ComponentScan
import io.github.msengbusch.unitsystem.component.PreComponent
import io.github.msengbusch.unitsystem.extension.ExtensionOutput
import io.github.msengbusch.unitsystem.extension.ExtensionScan
import io.github.msengbusch.unitsystem.extension.PreExtension
import io.github.msengbusch.unitsystem.module.ModuleOutput
import io.github.msengbusch.unitsystem.module.ModuleScan
import io.github.msengbusch.unitsystem.module.PreModule

class Processor(env: SymbolProcessorEnvironment) : SymbolProcessor {
    private val logger = env.logger
    private val generator = env.codeGenerator

    private val scannedComponents = mutableMapOf<String, PreComponent>()
    private val scannedExtensions = mutableMapOf<String, PreExtension>()
    private val scannedModules = mutableMapOf<String, PreModule>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        scannedComponents.putAll(ComponentScan.scanComponents(resolver, logger))
        scannedExtensions.putAll(ExtensionScan.scanExtensions(resolver, logger))
        scannedModules.putAll(ModuleScan.scanModule(resolver, logger))

        return emptyList()
    }

    override fun finish() {
        ComponentOutput.generateOutput(scannedComponents.map { it.value }, generator, logger)
        ExtensionOutput.generateOutput(scannedExtensions.map { it.value }, generator, logger)
        ModuleOutput.generateOutput(scannedModules.map { it.value }, generator, logger)
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment)
    }
}
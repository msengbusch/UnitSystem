package io.github.msengbusch.unitsystem

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.msengbusch.unitsystem.unit.UnitScan

class Processor(private val env: SymbolProcessorEnvironment) : SymbolProcessor {
    private val logger = env.logger

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val preUnits = UnitScan.scanForUnit(resolver, logger)

        return emptyList()
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment)
    }

}
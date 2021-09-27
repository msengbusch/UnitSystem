package io.github.msengbusch.unitsystem.module

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.msengbusch.unitsystem.unit.Module

object ModuleScan {
    fun scanModule(resolver: Resolver, logger: KSPLogger): Map<String, PreModule> {
        return resolver.getSymbolsWithAnnotation(Module::class.qualifiedName!!)
            .map { processElement(it, logger) }
            .associateBy { it.className }
    }

    @OptIn(KspExperimental::class)
    private fun processElement(element: KSAnnotated, logger: KSPLogger): PreModule {
        if (element !is KSClassDeclaration) {
            throw IllegalArgumentException("@Module can't be applied to $element: must be a class")
        }

        val file = element.containingFile!!
        val className = element.qualifiedName!!.asString()

        return PreModule(file, className)
    }
}
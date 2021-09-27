package io.github.msengbusch.unitsystem.extension

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.msengbusch.unitsystem.unit.Extension

object ExtensionScan {
    fun scanExtensions(resolver: Resolver, logger: KSPLogger): Map<String, PreExtension> {
        return resolver.getSymbolsWithAnnotation(Extension::class.qualifiedName!!)
            .map { processElement(it, logger) }
            .associateBy { it.className }
    }

    @OptIn(KspExperimental::class)
    private fun processElement(element: KSAnnotated, logger: KSPLogger): PreExtension {
        if (element !is KSClassDeclaration) {
            throw IllegalArgumentException("@Extension can't be applied to $element: must be a class")
        }

        val file = element.containingFile!!
        val className = element.qualifiedName!!.asString()

        return PreExtension(file, className)
    }
}
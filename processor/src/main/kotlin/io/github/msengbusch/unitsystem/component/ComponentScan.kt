package io.github.msengbusch.unitsystem.component

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.msengbusch.unitsystem.unit.Component

object ComponentScan {
    fun scanComponents(resolver: Resolver, logger: KSPLogger): Map<String, PreComponent> {
        return resolver.getSymbolsWithAnnotation(Component::class.qualifiedName!!)
            .map { processElement(it, logger) }
            .associateBy { it.className }
    }

    @OptIn(KspExperimental::class)
    private fun processElement(element: KSAnnotated, logger: KSPLogger): PreComponent {
        if (element !is KSClassDeclaration) {
            throw IllegalArgumentException("@Component can't be applied to $element: must be a class")
        }

        val file = element.containingFile!!
        val className = element.qualifiedName!!.asString()

        return PreComponent(file, className)
    }
}
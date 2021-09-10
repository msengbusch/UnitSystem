package io.github.msengbusch.unitsystem.unit

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import io.github.msengbusch.unitsystem.exception.AlreadyExistingException
import io.github.msengbusch.unitsystem.exception.IllegalAnnotationException
import io.github.msengbusch.unitsystem.exception.MissingAnnotationException
import io.github.msengbusch.unitsystem.util.ClassName
import io.github.msengbusch.unitsystem.util.Name
import javax.inject.Inject

object UnitScan {
    fun scanForUnit(resolver: Resolver, logger: KSPLogger): Map<ClassName, PreUnit> {
        val units = mutableMapOf<Name, PreUnit>()

        resolver.getSymbolsWithAnnotation(ValidUnit::class.qualifiedName!!).forEach { element ->
            val unit = processElements(element, logger)

            if (units.contains(unit.name)) {
                throw AlreadyExistingException("$unit has the same name as unit from class ${units[unit.name]!!.className}")
            }

            units[unit.className] = unit

            logger.info("Found $unit", element)
        }

        return units
    }

    @OptIn(KspExperimental::class)
    private fun processElements(element: KSAnnotated, logger: KSPLogger): PreUnit {
        if (element !is KSClassDeclaration) {
            throw IllegalAnnotationException("@Unit can't be applied to $element: must be a class")
        }

        val annotations = element.getAnnotationsByType(Unit::class).toList()

        if (annotations.isEmpty()) {
            throw IllegalArgumentException("Class $element is not annotated with @Unit. This method shouldn't be called. Notify the author")
        }

        if (annotations.size > 1) {
            throw IllegalAnnotationException("Class $element is annotated ${annotations.size} times: must be one time")
        }

        val annotation = annotations[0]

        val className: ClassName = element.qualifiedName!!.asString()
        val name: Name = annotation.name

        val isComponent: Boolean = annotation.component
        val inheritable: Boolean = annotation.inheritable

        val before: List<Name> = annotation.before.toList()
        val after: List<Name> = annotation.after.toList()

        val parentClasses: List<KSTypeReference> = element.superTypes.toList()

        val isInstanciable: Boolean = !element.isAbstract()

        val parameters: List<KSValueParameter>
        if (isInstanciable) {
            val constructors = element.getConstructors()
                .filter { it.isAnnotationPresent(Inject::class) }
                .toList()

            if (constructors.isEmpty()) {
                throw MissingAnnotationException("Unit $className does not have a constructor annotated with @Inject")
            }

            if (constructors.size > 1) {
                throw IllegalAnnotationException("Unit $className has more than one constructor annotated with @Inject")
            }

            parameters = constructors[0].parameters
        } else {
            parameters = emptyList()
        }

        return PreUnit(
            name,
            className,
            before,
            after,
            parentClasses,
            parameters,
            isComponent,
            inheritable,
            isInstanciable
        )
    }
}
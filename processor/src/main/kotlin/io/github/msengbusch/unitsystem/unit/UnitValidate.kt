package io.github.msengbusch.unitsystem.unit

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.msengbusch.unitsystem.exception.NotExistingException
import io.github.msengbusch.unitsystem.util.ClassName

object UnitValidate {
    fun validateUnits(preUnits: Map<ClassName, PreUnit>, logger: KSPLogger): Map<ClassName, ValidUnit> {
        return preUnits.map { (className, preUnit) ->
            val unit = processUnit(preUnit, preUnits, logger)

            logger.info("Validated $unit")

            Pair(className, unit)
        }.toMap()
    }

    @OptIn(KspExperimental::class)
    private fun processUnit(preUnit: PreUnit, preUnits: Map<ClassName, PreUnit>, logger: KSPLogger): ValidUnit {
        val before = preUnit.before.map { beforeName ->
            preUnits.values.find { it.name == beforeName }
                ?: throw NotExistingException("$preUnit declares unknown before dependency: $beforeName")
        }.toMutableList()

        val after = preUnit.after.map { afterName ->
            preUnits.values.find { it.name == afterName }
                ?: throw NotExistingException("$preUnit declares unknown after dependency: $afterName")
        }

        val inherited = preUnit.parentClasses
            .filter {
                it.annotations.find { annotation -> annotation.shortName.asString() == "Unit" } != null
            }
            .map {
                val className = (it.parent as KSClassDeclaration).qualifiedName!!.asString()
                preUnits[className]
                    ?: throw NotExistingException("$preUnit inherits $className which is annotated as @Unit which wasn't found during scan")
            }

        val components = preUnit.constructorParams
            .filter {
                it.annotations.find { annotation -> annotation.shortName.asString() == "Unit" } != null
            }
            .map {
                val typeClassName = (it.type.resolve() as KSClassDeclaration).qualifiedName!!.asString()

                preUnits[typeClassName]
                    ?: throw NotExistingException("$preUnit uses component unit $typeClassName in constructor which wasn't found during scan")
            }

        inherited.forEach { unit ->
            if (!before.contains(unit)) {
                before.add(unit)
            }
        }

        components.forEach { unit ->
            if (!before.contains(unit)) {
                before.add(unit)
            }
        }

        return ValidUnit(preUnit, before, after, inherited, components)
    }
}
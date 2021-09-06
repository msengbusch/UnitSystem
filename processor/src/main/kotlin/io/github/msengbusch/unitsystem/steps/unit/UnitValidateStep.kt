package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.context.Context
import io.github.msengbusch.unitsystem.step.Step
import io.github.msengbusch.unitsystem.steps.event.EventScanStep
import io.github.msengbusch.unitsystem.steps.unit.data.UnitName
import io.github.msengbusch.unitsystem.steps.unit.data.ValidUnit
import io.github.msengbusch.unitsystem.util.debug

class UnitValidateStep : Step {
    val units = mutableMapOf<UnitName, ValidUnit>()

    override fun process(context: Context) {
        val rawEvents = context.getStep(EventScanStep::class.java).events
        val rawUnits = context.getStep(UnitScanStep::class.java).units

        rawUnits.forEach { (className, rawUnit) ->
            val before = rawUnit.before.map{ beforeName ->
                if(!rawUnits.containsKey(beforeName)) {
                    throw IllegalArgumentException("$rawUnit declares unknown before dependency: $beforeName")
                }

                rawUnits[beforeName]!!
            }

            val after = rawUnit.after.map{ afterName ->
                if(!rawUnits.containsKey(afterName)) {
                    throw IllegalArgumentException("$rawUnit declares unknown after dependency: $afterName")
                }

                val after = rawUnits[afterName]!!

                if(before.contains(after)) {
                    throw IllegalArgumentException("$rawUnit declares the same unit as before and after dependency: $afterName")
                }

                after
            }

            val foundEvents = rawUnit.parentClasses
                .stream()
                .filter { parentClass -> rawEvents.containsKey(parentClass) }
                .peek { parentClass -> context.debug("Parent class $parentClass of Unit $className is an event class") }
                .map { parentClass -> rawEvents[parentClass]!! }
                .toList()

            val unit = ValidUnit(rawUnit, before, after, foundEvents)
            units[unit.name] = unit

            context.debug("Validate $unit")
        }
    }
}
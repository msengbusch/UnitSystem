package io.github.msengbusch.unitsystem.stepsold.unit

import io.github.msengbusch.unitsystem.context.Context
import io.github.msengbusch.unitsystem.step.Step
import io.github.msengbusch.unitsystem.stepsold.event.EventScanStep
import io.github.msengbusch.unitsystem.util.writeToResource

class UnitOutputStep : Step {
    override fun process(context: Context) {
        val events = context.getStep(EventScanStep::class.java).events
        val units = context.getStep(UnitSortStep::class.java).units

        val lines = mutableListOf<String>()

        events.forEach { (_, event) ->
            lines.add("[Event]")
            lines.add("class=${event.className}")
        }

        units.forEach { unit ->
            lines.add("[Unit]")
            lines.add("name=${unit.name}")
            lines.add("class=${unit.className}")
            if(unit.before.isNotEmpty()) lines.add("before=${unit.before.joinToString(",") { it.name }}")
            if(unit.after.isNotEmpty()) lines.add("after=${unit.after.joinToString(",")  { it.name }}")
            if(unit.events.isNotEmpty()) lines.add("events=${unit.events.joinToString(",") { it.className }}")
        }

        if(units.isNotEmpty()) {
            lines.add("[Order]")
            lines.add("run=${units.joinToString(",") { it.name }}")
        }

        context.processingEnv.writeToResource("units", lines)
    }
}
package io.github.msengbusch.unitsystem.step

import io.github.msengbusch.unitsystem.context.Context

interface Step {
    fun process(context: Context)
}
package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.Context

interface Constructor {
    fun construct(context: Context)
}
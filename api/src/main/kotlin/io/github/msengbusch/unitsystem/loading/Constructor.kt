package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.Context
import io.github.msengbusch.unitsystem.unit.Container
import io.github.msengbusch.unitsystem.unit.Instance

interface Constructor {
    fun constructAll(context: Context)

    fun <T> construct(container: Container<T>): Instance<T>
}
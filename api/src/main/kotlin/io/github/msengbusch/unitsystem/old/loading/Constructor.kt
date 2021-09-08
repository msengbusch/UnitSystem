package io.github.msengbusch.unitsystem.old.loading

import io.github.msengbusch.unitsystem.old.Context
import io.github.msengbusch.unitsystem.old.unit.Container
import io.github.msengbusch.unitsystem.old.unit.Instance

interface Constructor {
    fun constructAll(context: Context)

    fun <T> construct(container: Container<T>): Instance<T>
}
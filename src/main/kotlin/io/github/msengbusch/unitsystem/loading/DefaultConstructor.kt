package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.di.Scope
import io.github.msengbusch.unitsystem.unit.Container
import io.github.msengbusch.unitsystem.unit.DefaultInstance
import io.github.msengbusch.unitsystem.unit.Instance

class DefaultConstructor(private val scope: Scope) : Constructor {
    override fun <T> construct(container: Container<T>): Instance<T> {
        val instance = scope.getInstance(container.clazz)
        return DefaultInstance<T>(container, instance)
    }
}
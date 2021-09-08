package io.github.msengbusch.unitsystem.old.scope

interface ChildScope : Scope {

    fun excludeParentInjection(clazz: Class<*>)
}
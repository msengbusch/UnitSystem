package io.github.msengbusch.unitsystem.di

interface ChildScope : Scope {

    fun excludeParentInjection(clazz: Class<*>)
}
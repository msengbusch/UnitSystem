package io.github.msengbusch.unitsystem.di

class DefaultChildScope(private val parent: Scope) : AbstractScope(), ChildScope {
    private val excludedInjections: MutableList<Class<*>> = mutableListOf()

    override fun excludeParentInjection(clazz: Class<*>) {
        excludedInjections.add(clazz)
    }

    override fun <T> createNewInstance(clazz: Class<T>): T {
        if (!excludedInjections.contains(clazz) && parent.isTypeKnown(clazz)) {
            return parent.getInstance(clazz)
        }

        return super.createNewInstance(clazz)
    }
}
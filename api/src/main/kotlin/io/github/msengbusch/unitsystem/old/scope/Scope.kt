package io.github.msengbusch.unitsystem.old.scope

import javax.inject.Provider

interface Scope {
    fun <T> bindProvider(clazz: Class<T>, provider: Provider<T>)
    fun <T> bindSingleton(clazz: Class<T>, instance: T) where T : Any
    fun <T> bindInterface(interfaceClass: Class<T>, implementationClass: Class<out T>)

    fun <T> getInstance(clazz: Class<T>): T

    fun isTypeKnown(clazz: Class<*>): Boolean
}
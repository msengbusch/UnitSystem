package io.github.msengbusch.unitsystem.di

import javax.inject.Provider

interface InjectionScope {
    fun <T> bindProvider(clazz: Class<T>, provider: Provider<T>)
    fun <T> bindSingleton(clazz: Class<T>, instance: T) where T : Any
    fun <T> bindInterface(interfaceClass: Class<T>, implementationClass: Class<out T>)

    fun <T> getInstance(clazz: Class<T>): T
}
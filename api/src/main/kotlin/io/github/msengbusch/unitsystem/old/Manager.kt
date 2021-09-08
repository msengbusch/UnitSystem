package io.github.msengbusch.unitsystem.old

import java.util.function.Consumer

interface Manager {
    fun <T> callEvent(clazz: Class<T>, function: Consumer<T>)
}
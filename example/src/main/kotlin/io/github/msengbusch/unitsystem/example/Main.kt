package io.github.msengbusch.unitsystem.example

import io.github.msengbusch.unitsystem.loading.UnitLoader

fun main() {
    val loader = UnitLoader()
    val context = loader.loadContext()

    context.unitEvents.forEach { (_, event) ->
        println("Unit Event: ")
        println("Class: ${event.clazz}")
        println("----------------------")
    }

    context.units.forEach { (_, unit) ->
        println("Unit: ")
        println("Name: ${unit.name}")
        println("Class: ${unit.clazz}")
        unit.events?.forEach { event ->
            println("Unit - Event: ")
            println("Class: ${event.clazz}")
        }
        println("----------------------")
    }
}
package io.github.msengbusch.unitsystem.example

import io.github.msengbusch.unitsystem.loading.DefaultLoader
import io.github.msengbusch.unitsystem.loading.DefaultParser

fun main() {
    val parser = DefaultParser()
    parser.parse(DefaultParser.defaultUnitFileContent())

    val loader = DefaultLoader()
    val context = loader.loadContext(parser)

    context.events.forEach { (_, event) ->
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
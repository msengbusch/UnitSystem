package io.github.msengbusch.unitsystem.example

import io.github.msengbusch.unitsystem.DefaultManager
import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.example.event.UnitReload
import io.github.msengbusch.unitsystem.old.loading.DefaultConstructor
import io.github.msengbusch.unitsystem.old.loading.DefaultLoader
import io.github.msengbusch.unitsystem.old.loading.DefaultParser
import io.github.msengbusch.unitsystem.old.scope.DefaultScope

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

    val scope = DefaultScope()
    val constructor = DefaultConstructor(scope)

    constructor.constructAll(context)

    val manager = DefaultManager(context)
    manager.callEvent(UnitCycle::class.java) {
        it.enable()
    }

    manager.callEvent(UnitCycle::class.java) {
        it.disable()
    }

    manager.callEvent(UnitReload::class.java) {
        it.reload()
    }
}
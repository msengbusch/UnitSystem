package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.UnitContext
import io.github.msengbusch.unitsystem.event.UnitEventContainer
import io.github.msengbusch.unitsystem.exception.UnitLoadingException
import io.github.msengbusch.unitsystem.unit.UnitContainer
import io.github.msengbusch.unitsystem.util.UNIT_FILE_GRADLE_PATH
import io.github.msengbusch.unitsystem.util.UNIT_FILE_JAR_RESOURCE
import io.github.msengbusch.unitsystem.util.contentFromResource
import java.nio.file.Files
import kotlin.io.path.Path

class UnitLoader {
    fun loadContext(): UnitContext {
        val content: List<String>

        val gradlePath = Path(UNIT_FILE_GRADLE_PATH)
        content = if (Files.exists(gradlePath)) {
            Files.readAllLines(gradlePath)
        } else {
            contentFromResource(UNIT_FILE_JAR_RESOURCE)
        }

        return loadContext(content)
    }

    private fun loadContext(content: List<String>): UnitContext {
        val parser = UnitParser()
        parser.parse(content)

        val unitEvents = loadUnitEvents(parser)
        val units = loadUnits(parser, unitEvents.mapKeys { (_, value) -> value.clazz.name })

        return UnitContext(unitEvents, units)
    }

    private fun loadUnitEvents(parser: UnitParser): Map<Class<*>, UnitEventContainer> {
        val unitEvents = mutableMapOf<Class<*>, UnitEventContainer>()

        parser.unitEventEntries.forEach { entry ->
            val className = entry.className!!
            val clazz: Class<*>

            try {
                clazz = this::class.java.classLoader.loadClass(className)
            } catch (e: ClassNotFoundException) {
                throw UnitLoadingException("Could not find class for $entry")
            }

            val unitEvent = UnitEventContainer(clazz)
            unitEvents[clazz] = unitEvent
        }

        return unitEvents
    }

    private fun loadUnits(
        parser: UnitParser,
        unitEvents: Map<String, UnitEventContainer>
    ): Map<Class<*>, UnitContainer> {
        val units = mutableMapOf<Class<*>, UnitContainer>()

        parser.unitEntries.forEach { entry ->
            val clazz: Class<*>
            try {
                clazz = this::class.java.classLoader.loadClass(entry.className)
            } catch (e: ClassNotFoundException) {
                throw UnitLoadingException("Could not find class for $entry")
            }

            val eventContainers = entry.events?.map { event ->
                if (!unitEvents.containsKey(event)) {
                    throw UnitLoadingException("$entry requests unknown UnitEvent: $event")
                }

                unitEvents[event]!!
            }

            val unit = UnitContainer(entry.name!!, clazz, eventContainers)
            units[clazz] = unit
        }

        return units
    }
}
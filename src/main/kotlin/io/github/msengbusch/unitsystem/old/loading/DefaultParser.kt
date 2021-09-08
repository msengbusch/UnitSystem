package io.github.msengbusch.unitsystem.old.loading

import io.github.msengbusch.unitsystem.exception.UnitException
import io.github.msengbusch.unitsystem.util.UNIT_FILE_GRADLE_PATH
import io.github.msengbusch.unitsystem.util.UNIT_FILE_JAR_RESOURCE
import io.github.msengbusch.unitsystem.util.contentFromResource
import java.nio.file.Files
import kotlin.io.path.Path

class DefaultParser : Parser {
    private var currentEntry = Entry()

    override val events: MutableList<Parser.EventEntry> = mutableListOf()
    override val units: MutableList<Parser.UnitEntry> = mutableListOf()

    override fun parse(content: List<String>) {
        content.forEach { rawLine ->
            val line = rawLine.trim()

            val skip = when (line) {
                "[Event]" -> newEntry(ParsingEventEntry())
                "[Unit]" -> newEntry(ParsingUnitEntry())
                "[Order]" -> true
                else -> false
            }

            if (skip) {
                return@forEach
            }

            val split = line.split("=")
            if (split.size == 1) {
                throw UnitException("Expected key=value line: $line")
            }

            if (split.size > 2) {
                throw UnitException("Expected only one = per line: $line")
            }

            currentEntry.read(split[0], split[1])
        }

        currentEntry.finish()
    }

    private fun newEntry(entry: Entry): Boolean {
        currentEntry.finish()
        currentEntry = entry
        return true
    }

    open class Entry {
        open fun read(key: String, value: String) {}
        open fun finish() {}
    }

    inner class ParsingEventEntry : Entry(), Parser.EventEntry {
        override var className: String? = null

        override fun read(key: String, value: String) {
            when (key) {
                "class" -> className = value
            }
        }

        override fun finish() {
            if (className == null) {
                throw UnitException("One event unit entry does not contain the class name")
            }

            events.add(this)
        }

        override fun toString(): String {
            return "UnitEventEntry(className=$className)"
        }
    }

    inner class ParsingUnitEntry : Entry(), Parser.UnitEntry {
        override var name: String? = null
        override var className: String? = null
        override var events: List<String>? = null
        override var before: List<String>? = null
        override var after: List<String>? = null

        override fun read(key: String, value: String) {
            when (key) {
                "name" -> name = value
                "class" -> className = value
                "events" -> events = value.split(",")
                "before" -> before = value.split(",")
                "after" -> after = value.split(",")
            }
        }

        override fun finish() {
            if (name == null) {
                throw UnitException("$this does not include a name")
            }

            if (className == null) {
                throw UnitException("$this does not include a class")
            }

            units.add(this)
        }

        override fun toString(): String {
            return "UnitEntry(name=$name, className=$className, events=$events, before=$before, after=$after)"
        }
    }

    companion object {
        fun defaultUnitFileContent(): List<String> {
            val gradleFile = Path(UNIT_FILE_GRADLE_PATH)
            if (Files.exists(gradleFile)) {
                return Files.readAllLines(gradleFile)
            }

            return contentFromResource(UNIT_FILE_JAR_RESOURCE)
        }
    }
}
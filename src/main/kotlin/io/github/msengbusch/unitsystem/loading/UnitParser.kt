package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.exception.UnitLoadingException

class UnitParser {
    private var currentEntry: Entry = Entry()

    val unitEventEntries: MutableList<UnitEventEntry> = mutableListOf()
    val unitEntries: MutableList<UnitEntry> = mutableListOf()

    fun parse(content: List<String>) {
        content.forEach { line ->
            val skip = when (line.trim()) {
                "[UnitEvent]" -> newEntry(UnitEventEntry())
                "[Unit]" -> newEntry(UnitEntry())
                else -> false
            }

            if (skip) {
                return@forEach
            }

            val split = line.split("=")
            if (split.size == 1) {
                throw UnitLoadingException("Expected key=value line: $line")
            }

            if (split.size > 2) {
                throw UnitLoadingException("Expected only one = per line: $line")
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

    inner class UnitEventEntry : Entry() {
        var className: String? = null

        override fun read(key: String, value: String) {
            when (key) {
                "class" -> className = value
            }
        }

        override fun finish() {
            if (className == null) {
                throw UnitLoadingException("One event unit entry does not contain the class name")
            }

            unitEventEntries.add(this)
        }

        override fun toString(): String {
            return "UnitEventEntry(className=$className)"
        }


    }

    inner class UnitEntry : Entry() {
        var name: String? = null
        var className: String? = null
        var events: List<String>? = null

        override fun read(key: String, value: String) {
            when (key) {
                "name" -> name = value
                "class" -> className = value
                "events" -> events = value.split(",")
            }
        }

        override fun finish() {
            if (name == null) {
                throw UnitLoadingException("$this does not include a name")
            }

            if (className == null) {
                throw UnitLoadingException("$this does not include a class")
            }

            unitEntries.add(this)
        }

        override fun toString(): String {
            return "UnitEntry(name=$name, className=$className, events=$events)"
        }
    }
}
package io.github.msengbusch.unitsystem.old.loading

interface Parser {
    val events: List<EventEntry>
    val units: List<UnitEntry>

    fun parse(content: List<String>)

    interface EventEntry {
        val className: String?
    }

    interface UnitEntry {
        val name: String?
        val className: String?
        val events: List<String>?
        val before: List<String>?
        val after: List<String>?
    }
}
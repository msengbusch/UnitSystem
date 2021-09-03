package io.github.msengbusch.unitsystem.loading

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
    }
}
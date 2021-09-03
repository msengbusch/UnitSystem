package io.github.msengbusch.unitsystem.example.unit

import io.github.msengbusch.unitsystem.example.event.UnitCycle
import io.github.msengbusch.unitsystem.unit.Unit

@Unit("testUnit2")
class TestUnit2(override val size: Int) : List<String>, UnitCycle {
    override fun enable() {
        println("Test unit enable")
    }

    override fun disable() {
        println("Test unit disable")
    }

    override fun contains(element: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): String {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: String): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<String> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: String): Int {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<String> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<String> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<String> {
        TODO("Not yet implemented")
    }
}
package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.event.UnitEventContainer
import io.github.msengbusch.unitsystem.unit.UnitContainer
import io.kotest.core.spec.style.FunSpec
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.map
import test.TestUnit
import test.TestUnit2
import test.TestUnitEvent
import test.TestUnitEvent2
import util.GRADLE_UNIT_FILE
import util.contentFromFile

class UnitLoaderTest : FunSpec({
    test("testLoadContext") {
        val loader = UnitLoader()

        val context = loader.loadContext(contentFromFile(GRADLE_UNIT_FILE))

        expectThat(context.unitEvents.keys)
            .containsExactly(TestUnitEvent::class.java, TestUnitEvent2::class.java)

        expectThat(context.unitEvents.values)
            .map(UnitEventContainer::clazz)
            .containsExactly(TestUnitEvent::class.java, TestUnitEvent2::class.java)

        expectThat(context.units.keys)
            .containsExactly(TestUnit::class.java, TestUnit2::class.java)

        expectThat(context.units.values) {
            map(UnitContainer::name).containsExactly("testUnit", "testUnit2")
            map(UnitContainer::clazz).containsExactly(TestUnit::class.java, TestUnit2::class.java)
            // FIXME: map(UnitContainer::events).leng
        }
    }
})
package io.github.msengbusch.unitsystem.old.loading

import io.github.msengbusch.unitsystem.old.Context

interface Loader {
    fun loadContext(parser: Parser): Context
}
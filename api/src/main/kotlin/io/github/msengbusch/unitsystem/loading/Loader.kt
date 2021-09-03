package io.github.msengbusch.unitsystem.loading

import io.github.msengbusch.unitsystem.Context

interface Loader {
    fun loadContext(parser: Parser): Context
}
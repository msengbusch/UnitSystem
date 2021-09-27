package io.github.msengbusch.unitsystem.component

import com.google.devtools.ksp.symbol.KSFile

data class PreComponent(val file: KSFile, val className: String)
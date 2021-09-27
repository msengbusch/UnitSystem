package io.github.msengbusch.unitsystem.module

import com.google.devtools.ksp.symbol.KSFile

data class PreModule(val file: KSFile, val className: String)
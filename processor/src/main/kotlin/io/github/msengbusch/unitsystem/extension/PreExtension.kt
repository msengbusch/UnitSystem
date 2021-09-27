package io.github.msengbusch.unitsystem.extension

import com.google.devtools.ksp.symbol.KSFile

data class PreExtension(val file: KSFile, val className: String)
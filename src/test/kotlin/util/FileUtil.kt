package util

import java.nio.file.Files
import kotlin.io.path.Path

const val GRADLE_UNIT_FILE = "build/generated/source/kapt/test/units"

fun contentFromFile(path: String): List<String> {
    val reader = Files.newBufferedReader(Path(path))
    val lines = reader.readLines()

    reader.close()
    return lines
}
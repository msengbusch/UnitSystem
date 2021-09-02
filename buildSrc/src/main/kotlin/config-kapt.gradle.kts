plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
}

sourceSets {
    named("main") {
        resources {
            srcDir(layout.buildDirectory.dir("generated/source/kapt/main"))
        }
    }
}

kapt {
    includeCompileClasspath = false
}
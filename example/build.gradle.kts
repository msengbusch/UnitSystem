plugins {
    id("com.google.devtools.ksp") version "1.6.0-1.0.2"
}

dependencies {
    implementation(projects.unitSystem)

    ksp(projects.processor)
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.verbose = true
    }
}
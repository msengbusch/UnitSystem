plugins {
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
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
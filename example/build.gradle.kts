plugins {
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.ben-manes.versions") version "0.39.0"
}

val buildRelease: Provider<String> = providers.environmentVariable("BUILD_RELEASE").forUseAtConfigurationTime()
val buildNumber: Provider<String> = providers.environmentVariable("BUILD_NUMBER").forUseAtConfigurationTime()

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "msengbusch.github.io"
    version = "0.1.0"

    if (buildNumber.isPresent) {
        version = version as String + "-" + buildNumber.get()
    }

    if (!buildRelease.isPresent) {
        version = version as String + "-SNAPSHOT"
    }

    repositories {
        mavenCentral()
    }

    tasks {
        withType<JavaCompile> {
            targetCompatibility = JavaVersion.VERSION_16.toString()
            sourceCompatibility = JavaVersion.VERSION_16.toString()
        }

        withType<KotlinCompile> {
            kotlinOptions.javaParameters = true
            kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }

        named<Jar>("jar") {
            archiveClassifier.set("dev")
        }
    }
}

dependencies {
    api(projects.api)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

tasks {
    named("build") {
        dependsOn(named("jar"))
    }

    getByName<Test>("test") {
        useJUnitPlatform()
    }
}
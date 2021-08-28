import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version("1.5.30")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "msengbusch.github.io"
    version = "0.1.0-SNAPSHOT"

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
        }

        named<Jar>("jar") {
            archiveClassifier.set("dev")
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks {
    named("build") {
        dependsOn(named("jar"))
    }

    getByName<Test>("test") {
        useJUnitPlatform()
    }
}
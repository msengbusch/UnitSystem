import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")

    `config-kapt`
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
        }

        named<Jar>("jar") {
            archiveClassifier.set("dev")
        }
    }

    kotlin {
        kotlinDaemonJvmArgs = listOf(
            "-Dfile.encoding=UTF-8",
            "--show-version",
            "--enable-preview",
            "--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
        )
    }
}

dependencies {
    api(projects.annotations)

    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.strikt:strikt-core:0.32.0")

    kaptTest(projects.processor)
}

tasks {
    named("build") {
        dependsOn(named("jar"))
    }

    getByName<Test>("test") {
        useJUnitPlatform()
    }
}
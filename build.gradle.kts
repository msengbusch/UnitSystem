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
            kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
            targetCompatibility = JavaVersion.VERSION_16.toString()
            sourceCompatibility = JavaVersion.VERSION_16.toString()
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
    api(projects.api)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.assertj:assertj-core:3.20.2")

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
plugins {
    `config-kapt`
}

dependencies {
    implementation(projects.unitSystem)

    kapt(projects.processor)
}

tasks {
    named("processResources") {
        dependsOn(named("kaptKotlin"))
    }
}
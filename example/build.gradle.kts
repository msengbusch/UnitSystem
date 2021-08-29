plugins {
    `config-kapt`
}

dependencies {
    implementation(projects.unitSystem)

    kapt(projects.processor)
}
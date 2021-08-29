plugins {
    `config-kapt`
}

dependencies {
    implementation(projects.annotations)
    implementation("com.google.auto.service:auto-service:1.0")

    kapt("com.google.auto.service:auto-service:1.0")
}
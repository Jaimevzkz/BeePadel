plugins {
    alias(libs.plugins.beepadel.android.library)

    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.vzkz.connectivity.core.data"

}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)
}
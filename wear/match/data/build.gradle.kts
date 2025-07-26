plugins {
    alias(libs.plugins.beepadel.android.library)

}

android {
    namespace = "com.vzkz.wear.match.data"

    defaultConfig {
        minSdk = 30
    }

}

dependencies {
    implementation(libs.androidx.health.services.client)
    implementation(libs.bundles.koin)
    implementation(projects.wear.match.domain)
    implementation(projects.core.domain)
//    implementation(projects.core.connectivity.domain)

}
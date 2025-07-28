plugins {
    alias(libs.plugins.beepadel.android.application.wear.compose)

}

android {
    namespace = "com.vzkz.beepadel.wear.app"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.koin.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(projects.core.presentation.designsystemWear)
    implementation(projects.wear.match.presentation)
    implementation(projects.wear.match.data)
    implementation(projects.wear.match.domain)

    implementation(projects.core.data)
//    implementation(projects.core.connectivity.domain)
//    implementation(projects.core.connectivity.data)
}
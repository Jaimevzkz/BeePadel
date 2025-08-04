plugins {
    alias(libs.plugins.beepadel.android.library.compose)

}

android {
    namespace = "com.vzkz.beepadel.wear.presentation"
   defaultConfig {
        minSdk = 30
    }
}

dependencies {
    implementation(libs.androidx.wear)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.koin.compose)

    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.androidx.wear.compose.material)
    implementation(libs.androidx.wear.compose.ui.tooling)
    implementation(libs.play.services.wearable)

    implementation(projects.core.presentation.designsystemWear)
    implementation(projects.core.presentation.ui)
    implementation(projects.wear.match.domain)
    implementation(projects.match.domain)
    implementation(projects.match.presentation)
    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)

}
plugins {
    alias(libs.plugins.beepadel.android.library.compose)
}

android {
    namespace = "com.vzkz.beepadel.designsystem_wear"

    defaultConfig {
        minSdk = 30
    }

}

dependencies {
    api(projects.core.presentation.designsystem)
    implementation(libs.androidx.wear.compose.material)
    implementation(libs.androidx.material3)

}
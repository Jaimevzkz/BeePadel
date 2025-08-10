plugins {
    // Convention plugins
    alias(libs.plugins.beepadel.android.application.compose)
}

android {
    namespace = "com.vzkz.beepadel"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.database.data)
    implementation(projects.core.database.domain)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)

    implementation(projects.match.data)
    implementation(projects.match.domain)
    implementation(projects.match.presentation)

    implementation(projects.core.connectivity.domain)
    implementation(projects.core.connectivity.data)

    implementation(projects.settings.data)
    implementation(projects.settings.domain)
    implementation(projects.settings.presentation)

    implementation(projects.core.preferences.data)
    implementation(projects.core.preferences.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
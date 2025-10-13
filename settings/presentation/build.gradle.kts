plugins {
    alias(libs.plugins.beepadel.android.feature.ui)
}

android {
    namespace = "com.vzkz.beepadel.settings.presentation"
}

dependencies {
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.preferences.domain)
    implementation(projects.common.general)
}
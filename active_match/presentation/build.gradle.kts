plugins {
    alias(libs.plugins.beepadel.android.feature.ui)
}

android {
    namespace = "com.vzkz.active_match.presentation"
}

dependencies {
    implementation(projects.activeMatch.domain)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)

}
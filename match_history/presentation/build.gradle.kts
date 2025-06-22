plugins {
    alias(libs.plugins.beepadel.android.feature.ui)
}

android {
    namespace = "com.vzkz.match_history.presentation"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.matchHistory.domain)
}
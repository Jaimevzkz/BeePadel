plugins {
    alias(libs.plugins.beepadel.android.feature.ui)
}

android {
    namespace = "com.vzkz.match_history.presentation"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.matchHistory.domain)
}
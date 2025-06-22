plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.match_history.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.matchHistory.domain)
}
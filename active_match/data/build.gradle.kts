plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.active_match.data"

}

dependencies {
    implementation(projects.activeMatch.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}
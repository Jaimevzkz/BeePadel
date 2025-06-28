plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.match.data"

}

dependencies {
    implementation(projects.match.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}
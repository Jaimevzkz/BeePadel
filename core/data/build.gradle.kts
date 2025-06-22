plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.core.data"
}

dependencies {
    implementation(projects.core.domain)
}
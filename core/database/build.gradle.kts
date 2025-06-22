plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.core.database"
}

dependencies {
    implementation(projects.core.domain)
}
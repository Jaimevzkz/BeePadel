plugins {
    alias(libs.plugins.beepadel.android.library)
    alias(libs.plugins.beepadel.jvm.ktor)
}

android {
    namespace = "com.vzkz.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database.domain)
}
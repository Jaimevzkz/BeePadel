plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.common.general"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.match.domain)
    implementation(projects.core.database.domain)
    implementation(projects.core.connectivity.domain)
}

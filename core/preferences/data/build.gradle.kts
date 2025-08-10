plugins {
    alias(libs.plugins.beepadel.android.library)
    alias(libs.plugins.sqlDelight)
}

android {
    namespace = "com.vzkz.beepadel.core.preferences.data"
}

dependencies {
    implementation(projects.core.preferences.domain)
    implementation(projects.core.domain)

    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences)

}
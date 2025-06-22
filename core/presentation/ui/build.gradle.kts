plugins {
    alias(libs.plugins.beepadel.android.library.compose)

}

android {
    namespace = "com.vzkz.core.presentation.ui"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)

    implementation(projects.core.domain)
}
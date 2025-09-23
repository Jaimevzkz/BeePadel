plugins {
    alias(libs.plugins.beepadel.android.library)
}

android {
    namespace = "com.vzkz.core.notification"

}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)


}
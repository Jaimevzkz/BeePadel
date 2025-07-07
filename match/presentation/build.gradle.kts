plugins {
    alias(libs.plugins.beepadel.android.feature.ui)
}

android {
    namespace = "com.vzkz.match.presentation"
}

dependencies {
    implementation(projects.match.domain)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)
    testImplementation(testFixtures(projects.common.sharedTest))

}
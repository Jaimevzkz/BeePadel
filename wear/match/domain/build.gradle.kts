plugins {
    alias(libs.plugins.beepadel.jvm.library)

}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
//    implementation(projects.core.connectivity.domain)
}

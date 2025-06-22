plugins {
    alias(libs.plugins.beepadel.jvm.library)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
}

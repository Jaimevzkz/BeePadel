package com.vzkz.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension
){
    commonExtension.apply {
        buildFeatures.compose = true

        pluginManager.run {
            apply("org.jetbrains.kotlin.plugin.compose")
        }
    }

    dependencies{
        val bom = libs.findLibrary("androidx.compose.bom").get()
        "implementation"(platform(bom))
        "implementation"(libs.findBundle("compose").get())
        "implementation"(libs.findLibrary("androidx-ui-android").get())
        "androidTestImplementation"(platform(bom))
        "debugImplementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
        "debugImplementation"(libs.findLibrary("ui-tooling").get())
        "androidTestImplementation"(libs.findBundle("androidTest").get())

        // Navigation
        "implementation"(libs.findBundle("nav3").get())
        // Timber
        "implementation"(libs.findLibrary("timber").get())
    }
}
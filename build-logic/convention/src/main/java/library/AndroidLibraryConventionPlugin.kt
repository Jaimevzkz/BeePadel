package library

import com.android.build.api.dsl.LibraryExtension
import com.vzkz.build_logic.convention.ExtensionType
import com.vzkz.build_logic.convention.configureBuildTypes
import com.vzkz.build_logic.convention.configureKotlinAndroid
import com.vzkz.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
       target.run {
           pluginManager.run {
               apply("com.android.library")
               apply("org.jetbrains.kotlin.android")
           }

           extensions.configure<LibraryExtension>{
               configureKotlinAndroid(this)

               configureBuildTypes(
                   commonExtension = this,
                   extensionType = ExtensionType.LIBRARY
               )
               defaultConfig{
                   testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                   consumerProguardFiles("consumer-rules.pro")
               }
           }
           dependencies{
               "testImplementation"(kotlin("test"))
               "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())
               "implementation"(libs.findBundle("koin").get())
               "testImplementation"(libs.findBundle("test").get())
               // Timber
               "implementation"(project.libs.findLibrary("timber").get())
           }
       }
    }
}
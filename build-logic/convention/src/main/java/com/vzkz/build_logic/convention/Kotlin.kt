package com.vzkz.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
){
    commonExtension.apply {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
        defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    configureKotlin()

    dependencies{
        "coreLibraryDesugaring"(libs.findLibrary("desugar.jdk.libs").get())
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

internal fun Project.configureKotlinJvm(){
    extensions.configure<JavaPluginExtension>{
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    configureKotlin()
}

private fun Project.configureKotlin(){
    tasks.withType<KotlinCompile>().configureEach{
        compilerOptions{
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}
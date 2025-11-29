package com.vzkz.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }
        val localProps = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProps.load(localPropertiesFile.inputStream())
        }

        val notSet = "\"NOT-SET\""
        val clientID = System.getenv("STRAVA_CLIENT_ID")
            ?: localProps.getProperty("strava_client_id")

        val clientSecret = System.getenv("STRAVA_CLIENT_SECRET")
            ?: localProps.getProperty("strava_client_secret")

        defaultConfig {
            buildConfigField(
                "String",
                "APP_VERSION_NAME",
                "\"${libs.findVersion("projectVersionName").get()}\""
            )
            buildConfigField("String", "BASE_STRAVA_URL", "\"https://www.strava.com/api/v3\"")
            buildConfigField("Integer", "STRAVA_CLIENT_ID", clientID)
            buildConfigField("String", "STRAVA_CLIENT_SECRET", clientSecret)
            buildConfigField("String", "GITHUB_URL", "\"https://github.com/Jaimevzkz/BeePadel\"")
            buildConfigField("String", "CONTACT_EMAIL", "\"jaimevazquezmartin23@gmail.com\"")

            manifestPlaceholders["appAuthRedirectScheme"] = libs.findVersion("projectVersionName").get()
        }

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()
                        }
                        release {
                            configureReleaseBuildType(commonExtension)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()
                        }
                        release {
                            configureReleaseBuildType(commonExtension)
                        }
                    }
                }
            }
        }
    }
}


private fun BuildType.configureReleaseBuildType(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    isMinifyEnabled = false
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )

    // Here is where any build configuration for release would go (i.e. api_key, base_url)
}

private fun BuildType.configureDebugBuildType() {
}
package com.vzkz.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }


        val clientID =
            gradleLocalProperties(rootDir, rootProject.providers).getProperty("strava_client_id")
        defaultConfig {
            buildConfigField("String", "STRAVA_CLIENT_ID", clientID)
            manifestPlaceholders["appAuthRedirectScheme"] = "com.vzkz.beepadel"
//            manifestPlaceholders["appAuthRedirectScheme"] = libs.findVersion("projectVersionName").get()
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
                    defaultConfig {
                        buildConfigField(
                            "String",
                            "APP_VERSION_NAME",
                            "\"${libs.findVersion("projectVersionName").get()}\""
                        )
                    }
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
    buildConfigField("String", "BASE_STRAVA_URL", "\"https://strava-end-point\"")
    isMinifyEnabled = false
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )

    // Here is where any build configuration for release would go (i.e. api_key, base_url)
}

private fun BuildType.configureDebugBuildType() {
    buildConfigField("String", "BASE_STRAVA_URL", "\"https://strava-end-point\"")

}
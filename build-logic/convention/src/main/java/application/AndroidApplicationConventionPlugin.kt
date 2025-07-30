package application

import com.android.build.api.dsl.ApplicationExtension
import com.vzkz.build_logic.convention.ExtensionType
import com.vzkz.build_logic.convention.configureBuildTypes

import com.vzkz.build_logic.convention.configureKotlinAndroid
import com.vzkz.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension>{
                defaultConfig{
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

                    versionCode =  libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName =  libs.findVersion("projectVersionName").get().toString()
                }

                buildTypes{
                    release {
                        isMinifyEnabled = true
                    }
                }

                configureKotlinAndroid(this)

                configureBuildTypes(commonExtension = this, extensionType = ExtensionType.APPLICATION)
            }

        }
    }
}
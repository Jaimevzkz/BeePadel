package application

import com.android.build.api.dsl.ApplicationExtension
import com.vzkz.build_logic.convention.ExtensionType
import com.vzkz.build_logic.convention.configureBuildTypes
import com.vzkz.build_logic.convention.configureKotlinAndroid
import com.vzkz.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension>{
                defaultConfig.apply {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                    }
                }

                configureKotlinAndroid(this)

                configureBuildTypes(commonExtension = this, extensionType = ExtensionType.APPLICATION)
            }
            dependencies{
                "implementation"(project(":common:general"))
            }
        }
    }
}
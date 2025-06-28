package application

import com.android.build.api.dsl.ApplicationExtension
import com.vzkz.build_logic.convention.configureAndroidCompose
import com.vzkz.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            pluginManager.apply("beepadel.android.application")
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)


            dependencies{
                "implementation"(libs.findBundle("koin.compose").get())
            }
        }
    }
}
package application

import com.vzkz.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationWearComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("beepadel.android.application.compose")
            }

            dependencies {
                "implementation"(libs.findBundle("wear").get())
            }

        }
    }

}
package library

import com.android.build.api.dsl.LibraryExtension
import com.vzkz.build_logic.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("beepadel.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)

        }
    }
}
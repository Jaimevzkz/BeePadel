package feature

import com.android.build.api.dsl.LibraryExtension
import com.vzkz.build_logic.convention.ExtensionType
import com.vzkz.build_logic.convention.addUiLayerDependencies
import com.vzkz.build_logic.convention.configureBuildTypes
import com.vzkz.build_logic.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("beepadel.android.library.compose")
            }

            dependencies {
                "implementation"(project(":common:general"))
                addUiLayerDependencies(target)
            }
        }
    }
}
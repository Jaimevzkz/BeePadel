plugins {
    `kotlin-dsl`
}

group = "com.vzkz.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "beepadel.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "beepadel.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "beepadel.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "beepadel.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeatureUi") {
            id = "beepadel.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }

        register("jvmLibrary") {
            id = "beepadel.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}

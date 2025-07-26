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
            implementationClass = "application.AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "beepadel.android.application.compose"
            implementationClass = "application.AndroidApplicationComposeConventionPlugin"
        }


        register("androidApplicationWearCompose") {
            id = "beepadel.android.application.wear.compose"
            implementationClass = "application.AndroidApplicationWearComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "beepadel.android.library"
            implementationClass = "library.AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "beepadel.android.library.compose"
            implementationClass = "library.AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeatureUi") {
            id = "beepadel.android.feature.ui"
            implementationClass = "feature.AndroidFeatureUiConventionPlugin"
        }

        register("jvmLibrary") {
            id = "beepadel.jvm.library"
            implementationClass = "jvm.JvmLibraryConventionPlugin"
        }
    }
}

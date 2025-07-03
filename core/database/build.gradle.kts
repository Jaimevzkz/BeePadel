plugins {
    alias(libs.plugins.beepadel.android.library)
    alias(libs.plugins.sqlDelight)
}

android {
    namespace = "com.vzkz.core.database"
}

sqldelight {
    databases {
        create("BeePadelDB") {
            packageName.set("com.vzkz.core.database")
        }
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.match.domain)
    // SqlDelight
    implementation(libs.sqlDelight.androidDriver)
    implementation(libs.sqlDelight.coroutines)
}
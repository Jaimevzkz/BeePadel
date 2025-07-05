plugins {
    alias(libs.plugins.beepadel.android.library)
    alias(libs.plugins.sqlDelight)
}

android {
    namespace = "com.vzkz.core.database.data"
}

sqldelight {
    databases {
        create("BeePadelDB") {
            packageName.set("com.vzkz.core.database.data")
        }
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.match.domain)
    implementation(projects.core.database.domain)
    implementation(projects.common.general)
    testImplementation(testFixtures(projects.common.sharedTest))

    // SqlDelight
    implementation(libs.sqlDelight.androidDriver)
    testImplementation(libs.sqlDelight.sqliteDriver)
    implementation(libs.sqlDelight.coroutines)
}

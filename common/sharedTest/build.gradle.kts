plugins {
    alias(libs.plugins.beepadel.jvm.library)
    `java-test-fixtures`

}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies{

    implementation(projects.core.domain)
    implementation(projects.match.domain)
    implementation(projects.core.database.domain)

    testFixturesImplementation(libs.bundles.reducedTest)
}

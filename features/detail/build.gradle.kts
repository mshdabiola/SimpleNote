/*
 *abiola 2022
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("mshdabiola.android.feature")
    id("mshdabiola.android.library.compose")
    id("mshdabiola.android.library.jacoco")
}

android {
    namespace = "com.mshdabiola.detail"
}

dependencies {
    implementation(project(":modules:data"))
    implementation(project(":modules:domain"))

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(project(":modules:testing"))

    androidTestImplementation(project(":modules:testing"))
}

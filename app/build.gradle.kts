/*
 *abiola 2024
 */
import com.mshdabiola.app.BuildType


@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
//    alias(libs.plugins.kotlinAndroid)

    id("mshdabiola.android.application")
    id("mshdabiola.android.application.compose")
    id("mshdabiola.android.application.jacoco")
    //id("mshdabiola.android.application.flavor")
    id("mshdabiola.android.hilt")
    id("jacoco")
    id("mshdabiola.android.application.firebase")
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.roborazzi)

}

android {
    namespace = "com.mshdabiola.skeletonandroid"

    defaultConfig {
        applicationId = "com.mshdabiola.skeletonandroid"
        versionCode = 1
        versionName = "0.0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "com.mshdabiola.testing.TestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = BuildType.DEBUG.applicationIdSuffix
        }
        val release = getByName("release") {
            isMinifyEnabled = true
            applicationIdSuffix = BuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
           // signingConfig = signingConfigs.getByName("debug")
            // Ensure Baseline Profile is fresh for release builds.
            baselineProfile.automaticGenerationDuringBuild = true
        }
        create("benchmark") {
            // Enable all the optimizations from release build through initWith(release).
            initWith(release)
            matchingFallbacks.add("release")
            // Debug key signing is available to everyone.
            signingConfig = signingConfigs.getByName("debug")
            // Only use benchmark proguard rules
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = BuildType.BENCHMARK.applicationIdSuffix
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":modules:designsystem"))
    implementation(project(":modules:data"))
    implementation(project(":modules:ui"))


    implementation(project(":features:main"))
    implementation(project(":features:detail"))



    implementation(libs.timber)
    debugImplementation(libs.leakcanary.android)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
//    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    debugImplementation(libs.androidx.compose.ui.testManifest)
//    debugImplementation(projects.uiTestHiltManifest)

    kspTest(libs.hilt.compiler)

//    testImplementation(projects.core.dataTest)
    testImplementation(project(":modules:testing"))
    testImplementation(libs.accompanist.testharness)
    testImplementation(libs.hilt.android.testing)
//    testImplementation(libs.work.testing)

    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)

    androidTestImplementation(project(":modules:testing"))
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.accompanist.testharness)
    androidTestImplementation(libs.hilt.android.testing)
    debugImplementation (libs.androidx.monitor)
    baselineProfile(project(":benchmarks"))
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = true
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}
//
//android {
//
//    defaultConfig {
//        applicationId = "com.mshdabiola.skeletonandroid"
//
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//
//    }
//
//    buildTypes {
//        val debug by getting {
//            applicationIdSuffix = BuildType.DEBUG.applicationIdSuffix
//            versionNameSuffix = BuildType.DEBUG.versionNameSuffix
//        }
//        val release by getting {
//            isMinifyEnabled = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//
//            // To publish on the Play store a private signing key is required, but to allow anyone
//            // who clones the code to sign and run the release variant, use the debug signing key.
//            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
//            // Todo: comment code before release
////            signingConfig = signingConfigs.getByName("debug")
//            applicationIdSuffix = BuildType.RELEASE.applicationIdSuffix
//            versionNameSuffix = BuildType.RELEASE.versionNameSuffix
//        }
//        val benchmark by creating {
//            // Enable all the optimizations from release build through initWith(release).
//            initWith(release)
//            matchingFallbacks.add("release")
//            // Debug key signing is available to everyone.
//            signingConfig = signingConfigs.getByName("debug")
//            // Only use benchmark proguard rules
//            proguardFiles("benchmark-rules.pro")
//            //  FIXME enabling minification breaks access to demo backend.
//            isMinifyEnabled = false
//            applicationIdSuffix = BuildType.BENCHMARK.applicationIdSuffix
//            versionNameSuffix = BuildType.BENCHMARK.versionNameSuffix
//        }
////    compileOptions {
////        sourceCompatibility = JavaVersion.VERSION_1_8
////        targetCompatibility = JavaVersion.VERSION_1_8
////    }
////    kotlinOptions {
////        jvmTarget = "1.8"
////    }
////    buildFeatures {
////        compose = true
////    }
////    composeOptions {
////        kotlinCompilerExtensionVersion = "1.5.8"
////    }
////    packaging {
////        resources {
////            excludes += "/META-INF/{AL2.0,LGPL2.1}"
////        }
//    }
//}
////
//dependencies {
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//
//    implementation(libs.androidx.profileinstaller)
//    implementation(libs.androidx.core.splashscreen)
//
//    implementation(libs.timber)
//    debugImplementation (libs.leakcanary.android)
//
//}
//


//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//}
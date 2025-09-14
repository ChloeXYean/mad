// Top-level build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false // Ensure this is using Kotlin 2.2.20
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.2.20-2.0.3" apply false // <-- CORRECTED VERSION HERE
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
}
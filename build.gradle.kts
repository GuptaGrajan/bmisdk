

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.android.maven) apply false
    id ("maven-publish")
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false
    kotlin("jvm") version "2.0.20"

}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
}

buildscript {

    val objectboxVersion by extra("3.8.0")

    repositories {
        mavenCentral()
    }

    dependencies {
        // Android Gradle Plugin 3.4.0 or later supported.
        classpath(libs.gradle)
        classpath(libs.objectbox.gradle.plugin)
    }
}


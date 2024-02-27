// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

buildscript {

    val objectboxVersion by extra("3.8.0")

    repositories {
        mavenCentral()
    }

    dependencies {
        // Android Gradle Plugin 3.4.0 or later supported.
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("io.objectbox:objectbox-gradle-plugin:$objectboxVersion")
    }
}


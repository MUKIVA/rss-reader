plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mukiva.feature.search.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        create("profile") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.kotlin)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.impl)
    implementation(libs.coil.compose)

    implementation(libs.bundles.hilt.impl)
    kapt(libs.hilt.android.compiler)

    implementation(project(":navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
}
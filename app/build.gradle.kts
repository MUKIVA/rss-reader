plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("io.objectbox")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mukiva.rssreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mukiva.rssreader"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("profile") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            isDebuggable = false
            isProfileable = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.kotlin)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(platform(libs.okhttp.bom))

    implementation(libs.okhttp)
    implementation(libs.okhttp.interceptor)

    implementation(project(":navigation"))
    implementation(project(":data"))
    implementation(project(":core:utils"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:search:common"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:article_viewer"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

}
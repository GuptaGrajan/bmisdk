plugins {
    id ("com.android.library")
//    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)

}

android {
    signingConfigs {
        create("release") {
            /*keyAlias = "bmi2024"
            keyPassword = "bmi2024"
            storeFile = file("D:\\BMI Calculator\\app\\keystore\\bmiKey.jks")
            storePassword = "bmi2024"*/
        }

    }
    namespace = "com.app.bmicalculator"
    compileSdk = 34



    defaultConfig {
//        applicationId = "com.app.bmicalculator"
        minSdk = 24
        //noinspection OldTargetApi
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
//            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.play.services.auth.api.phone)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.httpclient)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.sdp.android)
    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.gson)
    implementation(libs.circularseekbar)

}
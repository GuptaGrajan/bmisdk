plugins {
    id ("com.android.library")
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id ("maven-publish")
    alias(libs.plugins.hilt.android)

}

android {
    namespace = "com.app.bmicalculator"
    compileSdk = 34



    defaultConfig {
        aarMetadata {
            minSdk = 24
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)        // << --- ADD This
        }
    }

    compileOptions  {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }


}
publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                groupId = "com.github.GuptaGrajan" // Your GitHub username or organization
                artifactId = "bmisdk" // Your library's artifact ID
                version = "1.0.7" // Your library's version
                from(components["release"])
            }
        }
//        https://github.com/GuptaGrajan/bmisdk.git
        repositories {
            mavenLocal()
        }

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
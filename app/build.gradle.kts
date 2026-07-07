plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.weatherapplication"
    compileSdk = 36

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.weatherapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Core & UI
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.swiperefreshlayout)

    // Architecture Components (Lifecycle & Navigation)
    implementation(libs.androidx.lifecycleRuntime)
    implementation(libs.androidx.lifecycleViewModel)
    implementation(libs.androidx.lifecycleLiveData)
    implementation(libs.androidx.navigationFragment)
    implementation(libs.androidx.navigationUi)

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Networking & Serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit.converterGson)
    implementation(libs.retrofit.serializationConverter)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serializationJson)

    // Storage & Utilities
    implementation(libs.androidx.datastorePreferences)
    implementation(libs.glide)
    implementation(libs.timber)
    implementation(libs.play.services.location)

    // Local Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutinesTest)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)

    // Instrumented Tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espressoCore)
}
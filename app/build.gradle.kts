plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")

    kotlin("plugin.serialization") version Versions.kotlin
    kotlin("kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "me.jeffreychang.giphy"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("debug") {
            isMinifyEnabled = false
        }
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
                )
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "me.jeffreychang.weatherapp"
}

dependencies {
    implementation("com.patrykandpatrick.vico:core:1.6.4")
    implementation("com.patrykandpatrick.vico:compose:1.6.4")
    implementation("com.patrykandpatrick.vico:core:1.6.4")

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-gif:2.2.2")

    implementation("androidx.fragment:fragment-ktx:1.5.5")
    implementation("com.google.dagger:hilt-android:${Versions.daggerHilt}")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")

    // Compose related libraries
    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")

    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.room:room-runtime:${Versions.room}")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:${Versions.room}")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:${Versions.room}")
    implementation("com.markodevcic:peko:3.0.1")

    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navComponent}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navComponent}")

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3")

    implementation("androidx.compose.ui:ui")

    implementation("androidx.activity:activity-compose:1.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")

    kapt("com.google.dagger:hilt-compiler:${Versions.daggerHilt}")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.9.0-alpha02")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.mockk:mockk:1.12.5")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
}
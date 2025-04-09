plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.adolfosalado.practicavn"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.adolfosalado.practicavn"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    val roomVersion = "2.6.1" // o la última versión estable
    val retrofit_version = "2.11.0"
    val lifecycle_version = "2.8.7"
    val corrutines_version = "1.10.1"
    val navigation_version = "2.7.5"
    val okhttp3_version = "4.9.0"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    implementation("androidx.room:room-runtime:$roomVersion")

    // Dependencias opcionales
    implementation("androidx.room:room-ktx:$roomVersion") // Extensiones Kotlin
    implementation("androidx.room:room-paging:$roomVersion") // Soporte para Paging 3
    testImplementation("androidx.room:room-testing:$roomVersion") // Utilidades para pruebas
    ksp("androidx.room:room-compiler:$roomVersion")

    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // RETROFIT
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:okhttp:$okhttp3_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp3_version") // For DEBUG

    //MVVM
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // CORRUTINAS
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$corrutines_version")

    // NAVIGATION
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")



}

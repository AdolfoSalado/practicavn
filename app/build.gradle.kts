plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.adolfosalado.practicavn"
    compileSdk = 34 // Usar la última versión estable del SDK (ej. 34)

    defaultConfig {
        applicationId = "com.adolfosalado.practicavn"
        minSdk = 24 // Usar un minSdk que soporte la mayoría de dispositivos (ej. 24)
        targetSdk = 34 // Usar la última versión estable del SDK (ej. 34)
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
        sourceCompatibility = JavaVersion.VERSION_17 // Usar Java 17
        targetCompatibility = JavaVersion.VERSION_17 // Usar Java 17
    }
    kotlinOptions {
        jvmTarget = "17" // Usar Java 17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // *** Versiones de Librerías ***
    val roomVersion = "2.6.1" // Usar la última versión estable de Room
    val retrofitVersion = "2.9.0" // Usar la última versión estable de Retrofit
    val lifecycleVersion = "2.7.0" // Usar la última versión estable de Lifecycle
    val coroutinesVersion = "1.7.3" // Usar la última versión estable de Coroutines
    val navigationVersion = "2.7.6" // Usar la última versión estable de Navigation
    val okhttp3Version = "4.12.0" // Usar la última versión estable de OkHttp3

    // *** Dependencias ***
    implementation("androidx.core:core-ktx:1.12.0") // Usar la última versión estable
    implementation("androidx.appcompat:appcompat:1.6.1") // Usar la última versión estable
    implementation("com.google.android.material:material:1.11.0") // Usar la última versión estable
    implementation("androidx.activity:activity-ktx:1.8.2") // Usar la última versión estable
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // *** Room ***
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Extensiones Kotlin
    ksp("androidx.room:room-compiler:$roomVersion")

    // *** Retrofit ***
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttp3Version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp3Version") // For DEBUG

    // *** MVVM ***
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // *** Corrutinas ***
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // *** Navigation ***
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // *** Hilt ***
    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")


// *** Test ***
    testImplementation("junit:junit:4.13.2") // JUnit para pruebas unitarias
    testImplementation("org.mockito:mockito-core:4.8.0") // Mockito para crear mocks
    testImplementation("com.google.truth:truth:1.1.3") // Truth para aserciones
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Coroutines test para probar coroutines
    testImplementation("androidx.arch.core:core-testing:2.2.0") // testing de los componentes de Android
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")// Mockito con Kotlin
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
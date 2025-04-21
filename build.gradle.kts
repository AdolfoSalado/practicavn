plugins {
    id("com.android.application") version "8.3.2" apply false // Última versión estable
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false // Versión estable de Kotlin (Asegurate de que es la misma que tienes instalada)
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false // Versión compatible con Kotlin 1.9.22
    id("com.google.dagger.hilt.android") version "2.48.1" apply false // Versión estable de Hilt
}
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Sesion 20: kapt, necesario para el procesador de anotaciones de Room.
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.upb.taskmanager"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.upb.taskmanager"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // Sesion 27: firma y generacion del APK/AAB de release.
    //
    // NOTA DIDACTICA: para simplificar el ejemplo, el build type "release"
    // reutiliza el signingConfig "debug" (una clave de depuracion que
    // Android Studio genera automaticamente en cada maquina, sin contrasena
    // que proteger). Esto permite generar un APK de release instalable
    // desde este proyecto sin pedir ni versionar un keystore real.
    //
    // Para publicar la app de verdad (por ejemplo en Google Play) hay que
    // generar un keystore propio (Build > Generate Signed Bundle / APK...
    // > Create new...) y usarlo aqui en vez del signingConfig "debug",
    // guardando esa clave y sus contrasenas fuera del control de versiones.
    signingConfigs {
        getByName("debug") {
            // Configuracion de depuracion por defecto de Android Studio.
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2023.05.01"))
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    // Sesion 08: Navigation Compose, para el grafo de navegacion entre pantallas.
    implementation("androidx.navigation:navigation-compose:2.6.0")
    // Sesion 12: integracion de ViewModel con Compose (funcion viewModel()).
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Sesion 14: Retrofit, para consumir servicios web REST.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Sesion 15: corrutinas de Kotlin sobre Android (Dispatchers.Main, etc).
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    // Sesion 20: Room, para persistencia local en una base de datos SQLite.
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    // Sesion 22: DataStore, para guardar preferencias simples del usuario.
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

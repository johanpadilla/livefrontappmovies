import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

val properties = Properties()
properties.load(project.rootProject.file("secret.properties").reader())

android {
    namespace = "com.example.livefront_app_movies"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.livefront_app_movies"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.livefront_app_movies.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        debug {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "ACCESS_TOKEN", properties.getProperty("ACCESS_TOKEN"))
            buildConfigField("String", "API_URL", properties.getProperty("API_URL"))
            buildConfigField("String", "IMAGE_URL", properties.getProperty("IMAGE_URL"))
            resValue("string", "clear_text_config","true")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "ACCESS_TOKEN", properties.getProperty("ACCESS_TOKEN"))
            buildConfigField("String", "API_URL", properties.getProperty("API_URL"))
            buildConfigField("String", "IMAGE_URL", properties.getProperty("IMAGE_URL"))
            resValue("string", "clear_text_config","false")
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
        compose = true
        buildConfig = true
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.moshi)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.google.dagger.hilt)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.date.formatter)

    ksp(libs.google.dagger.hilt.compiler)
    ksp(libs.moshi.kotlin.codegen)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.turbine)

    kspAndroidTest(libs.google.dagger.hilt.compiler)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.okhttp.espresso)
    androidTestImplementation(libs.androidx.test.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
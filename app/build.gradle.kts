plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.dd.callmonitor"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dd.callmonitor"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/io.netty.*"
        }
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(project(":ui"))

    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
}

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // TODO gonna use it?
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // TODO gonna use it?

    testImplementation(libs.junit.vintage)
    testImplementation(libs.koin.test)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation("androidx.compose.ui:ui-test-manifest") // TODO gonna use it?

    implementation("androidx.core:core-ktx:1.9.0") // TODO gonna use it?
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1") // TODO gonna use it?
    implementation("androidx.activity:activity-compose:1.7.2") // TODO gonna use it?
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.datastore.preferences)
    implementation(libs.fragment)
    implementation(libs.koin)
    implementation(libs.lifecycle.runtime.compose)
}

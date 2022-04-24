import com.duwei.buildsrc.Version
import com.duwei.buildsrc.Libs

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Version.compileSdk

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose.composeVersion
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
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Compose
    implementation(Libs.compose.activityCompose)
    implementation(Libs.compose.composeRuntime)
    implementation(Libs.compose.constraintLayout)
    implementation(Libs.compose.core)
    implementation(Libs.compose.foundation)
    implementation(Libs.compose.tooling)
    implementation(Libs.compose.layout)
    implementation(Libs.compose.material)
    implementation(Libs.compose.savedInstanceState)
    implementation(Libs.compose.uiLiveData)
    implementation(Libs.compose.composeNavigation)
    androidTestImplementation(Libs.compose.uiTest)
}
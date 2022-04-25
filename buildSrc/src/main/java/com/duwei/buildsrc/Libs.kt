package com.duwei.buildsrc

import org.gradle.api.JavaVersion


object Version {
    const val buildTools = ""
    const val versionCode = 1
    const val versionName = "1.0"

    const val minSdk = 21
    const val compileSdk = 31
    const val targetSdk = 26

    val java_version = JavaVersion.VERSION_11
    const val kotlin_version = "1.5.21"
    const val Coroutine_Core = "1.4.2"

    const val androidKtx = "1.0.0"
    const val appCompat = "1.3.1"
    const val constraintLayout = "2.0.0-beta3"

    const val navigation = "2.3.2"
    const val lifecycle = "2.2.0"
    const val room = "2.4.2"

    const val blockCanary = "1.5.0"
    const val fragment = "1.2.1"
    const val pagingVersion = "3.0.0-alpha11"

    object compose {
        const val composeVersion = "1.0.1"
        const val composeActivity = "1.3.1"
        const val composeConstraintLayout = "1.0.0-beta02"
        const val composeNavigation = "2.4.0-alpha06"
    }
}

object Libs {
    const val androidKtx = "androidx.core:core-ktx:${Version.androidKtx}"

    const val X_AppCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val X_ConstraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"

    const val X_Navigation_Fragment_Ktx = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val X_Navigation_Ui_Ktx = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    const val X_Lifecycle_Runtime = "androidx.lifecycle:lifecycle-runtime:${Version.lifecycle}"

    const val X_Lifecycle_Compiler = "androidx.lifecycle:lifecycle-compiler:${Version.lifecycle}"
    const val X_Room_Runtime = "androidx.room:room-runtime:${Version.room}"
    const val X_Room_Compiler = "androidx.room:room-compiler:${Version.room}"
    const val blockCanaryDebug = "com.github.markzhai:blockcanary-android:${Version.blockCanary}"

    const val blockCanaryRelease = "com.github.markzhai:blockcanary-no-op:${Version.blockCanary}"
    const val X_Lifecycle_ViewModel = "androidx.lifecycle:lifecycle-viewmodel:${Version.lifecycle}"

    const val X_Lifecycle_LiveData = "androidx.lifecycle:lifecycle-livedata:${Version.lifecycle}"
    const val X_Lifecycle_Extensions = "androidx.lifecycle:lifecycle-extensions:${Version.lifecycle}"
    const val X_Fragment = "androidx.fragment:fragment:${Version.fragment}"
    const val X_Fragment_Ktx = "androidx.fragment:fragment-ktx:${Version.fragment}"
    const val X_Fragment_Test = "androidx.fragment:fragment-testing:${Version.fragment}"
    const val X_Annotation = "androidx.annotation:annotation:1.0.2"

    const val Kotlin_Coroutine_Android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.Coroutine_Core}"
    const val Kotlin_Std_Jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin_version}"
    const val Kotlin_Coroutine_Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.Coroutine_Core}"
    const val Kotlin_Gradle_Plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10"

    const val X_Paging = "androidx.paging:paging-runtime:${Version.pagingVersion}"

    object AndroidX{
        object room{
            const val runtime = "androidx.room:room-runtime:${Version.room}"
            const val compiler = "androidx.room:room-compiler:${Version.room}"
            const val coroutine = "androidx.room:room-coroutines:2.1.0-alpha04"
        }
    }

    object compose {
        const val activityCompose = "androidx.activity:activity-compose:${Version.compose.composeActivity}"
        const val composeComplier = "androidx.compose:compose-compiler:${Version.compose.composeVersion}"
        const val composeNavigation = "androidx.navigation:navigation-compose:${Version.compose.composeNavigation}"
        const val composeRuntime = "androidx.compose.runtime:runtime:${Version.compose.composeVersion}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Version.compose.composeConstraintLayout}"
        const val core = "androidx.compose.ui:ui:${Version.compose.composeVersion}"
        const val foundation = "androidx.compose.foundation:foundation:${Version.compose.composeVersion}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Version.compose.composeVersion}"
        const val layout = "androidx.compose.foundation:foundation-layout:${Version.compose.composeVersion}"
        const val material = "androidx.compose.material:material:${Version.compose.composeVersion}"
        const val savedInstanceState = "androidx.compose.runtime:runtime-saveable:${Version.compose.composeVersion}"
        const val uiTest = "androidx.compose.ui:ui-test-junit4:${Version.compose.composeVersion}"
        const val uiLiveData = "androidx.compose.runtime:runtime-livedata:${Version.compose.composeVersion}"
    }

}

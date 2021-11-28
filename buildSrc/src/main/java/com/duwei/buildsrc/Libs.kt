package com.duwei.buildsrc

import org.gradle.api.JavaVersion


object Version {
    const val buildTools = ""
    const val versionCode = 1
    const val versionName = "1.0"

    const val minSdk = 21
    const val compileSdk = 28
    const val targetSdk = 26

    val java_version = JavaVersion.VERSION_1_8
    const val kotlin_version = "1.4.0"
    const val Coroutine_Core = "1.4.2"

    const val androidKtx = "1.0.0"
    const val appCompat = "1.0.2"
    const val constraintLayout = "2.0.0-beta3"

    const val navigation = "2.0.0"
    const val lifecycle = "2.2.0"
    const val room = "2.2.0"

    const val blockCanary = "1.5.0"
    const val fragment = "1.2.1"
    const val pagingVersion = "3.0.0-alpha11"
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

}

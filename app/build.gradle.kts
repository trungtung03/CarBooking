import org.apache.tools.ant.util.JavaEnvUtils.VERSION_11

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.carbooking"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.carbooking"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.glide)
    implementation(libs.circleimageview)
    implementation (libs.lifecycle.viewmodel.ktx)
    implementation(libs.fragment.ktx)

//    implementation(libs.design)
//    implementation(libs.support.vector.drawable)
//    implementation(libs.sdp.android)
    //noinspection OutdatedLibrary
    implementation(libs.play.services.maps)
//    implementation(libs.commons.lang3)
    //noinspection OutdatedLibrary
    implementation(libs.play.services.location)
    implementation(libs.play.services.places)
    implementation(libs.places)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
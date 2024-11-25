plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ma.saifdine.hd"
    compileSdk = 34

    defaultConfig {
        applicationId = "ma.saifdine.hd"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    dependencies {
        implementation(libs.lifecycle.livedata.ktx)
        implementation(libs.lifecycle.viewmodel.ktx)
        implementation(libs.room.runtime)
        annotationProcessor(libs.room.compiler)
    }

    // Dépendance pour FloatingActionButton
    implementation(libs.material.v190)

}
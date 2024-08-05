@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34 // Замените на целевую версию SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation (platform("com.google.firebase:firebase-bom:31.3.0"))
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
// Java language implementation
    implementation ("androidx.navigation:navigation-fragment:2.3.5")
    implementation ("androidx.navigation:navigation-ui:2.3.5")
    implementation ("com.hbb20:ccp:2.5.0")

    implementation ("com.github.dhaval2404:imagepicker:2.1")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(libs.firebase.firestore)

    implementation ("com.google.firebase:firebase-analytics:17.2.1")
    implementation (libs.firebase.database)
}
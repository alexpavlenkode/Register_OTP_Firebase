buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.3.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    id 'com.android.application' version '8.1.0' apply false
//    id 'com.android.library' version '8.1.0' apply false
    id("com.google.gms.google-services") version "4.3.3" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}
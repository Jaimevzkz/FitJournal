// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    //KSP
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    //FireBase
    id("com.google.gms.google-services") version "4.4.0" apply false
}

buildscript{
    dependencies{
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}

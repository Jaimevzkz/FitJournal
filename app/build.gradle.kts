plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //ksp
    id("com.google.devtools.ksp")
    //Hilt
    id("dagger.hilt.android.plugin")
    //FireBase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.vzkz.fitjournal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vzkz.fitjournal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            buildFeatures.buildConfig = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://api.api-ninjas.com/v1/exercises/\"") //This would be the url used in production

        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
            android.buildFeatures.buildConfig = true
            buildConfigField("String", "BASE_URL", "\"https://api.api-ninjas.com/v1/exercises/\"") //This would be the url used in debug
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //Unit Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.12.3")

    //Ui Test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Hilt
    val hiltVersion = "2.48.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")
    //Hilt navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    //Destinations
    val destinationsVersion = "1.9.54"
    implementation("io.github.raamcosta.compose-destinations:core:$destinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$destinationsVersion")

    //Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    //FireBase
    val bomVersion = "32.5.0"
    implementation(platform("com.google.firebase:firebase-bom:$bomVersion"))
    //Auth
    implementation("com.google.firebase:firebase-auth-ktx")
    //Analytics
    implementation("com.google.firebase:firebase-analytics")
    //FireStore
    implementation("com.google.firebase:firebase-firestore-ktx")

    //Animation
    implementation("androidx.compose.animation:animation:1.5.4")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.1")

    //Room
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

//    //room object JSON converter
//    implementation("com.google.code.gson:gson:2.10.1")


}
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

class Versions(private val rootProject: Project){
    val kotlinVersion:String get() = rootProject.extra.get("kotlinVersion") as String
    private val composeRelease = "dev10"
    val compose = "0.1.0-$composeRelease"
    val composeCompilerExtension = "0.1.0-$composeRelease"
    val appcompat = "1.1.0"
    val activityKtx = "1.1.0"
    val coreKtx = "1.2.0"
    val room = "2.2.5"
    val pref = "1.1.1"
    val lifecycle = "2.2.0"

    val junit = "4.13"
    val androidxTest = "1.2.0"
}



val versions = Versions(rootProject)

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "om.smartsoft.github"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {

        getByName("release") {
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
    kotlinOptions {
        jvmTarget = "1.8"
        apiVersion = "1.3"
        languageVersion = "1.3"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerVersion = "${versions.kotlinVersion}-dev-withExperimentalGoogleExtensions-20200424"
        kotlinCompilerExtensionVersion = versions.composeCompilerExtension
    }


}



dependencies {
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    with(versions) {

        implementation(project(":ssutil"))

        implementation(kotlin("stdlib-jdk7", kotlinVersion))
        implementation("androidx.appcompat", "appcompat", appcompat)

        implementation("androidx.compose", "compose-runtime", compose)

        implementation("androidx.ui", "ui-animation", compose)
        implementation("androidx.ui", "ui-framework", compose)
        implementation("androidx.ui", "ui-foundation", compose)
        implementation("androidx.ui", "ui-layout", compose)
        implementation("androidx.ui", "ui-material", compose)
        implementation("androidx.ui", "ui-platform", compose)
        implementation("androidx.ui", "ui-tooling", compose)
        implementation("androidx.ui", "ui-livedata", compose)

        implementation("androidx.core", "core-ktx", coreKtx)
        implementation("androidx.activity", "activity-ktx", activityKtx)

        implementation("androidx.room","room-runtime",room)
        implementation("androidx.room","room-ktx",room)
        implementation("androidx.preference","preference-ktx",pref)

        implementation("androidx.lifecycle","lifecycle-viewmodel-ktx",lifecycle)
        implementation("androidx.lifecycle","lifecycle-livedata-ktx",lifecycle)
        implementation("androidx.lifecycle","lifecycle-viewmodel-savedstate",lifecycle)
        kapt("androidx.lifecycle","lifecycle-compiler",lifecycle)

        implementation("com.squareup.retrofit2:retrofit:2.8.1")
        implementation("com.squareup.okhttp3:logging-interceptor:4.6.0")
        implementation("com.squareup.retrofit2:converter-gson:2.8.1")

        androidTestImplementation("junit","junit",junit)
        androidTestImplementation("androidx.test","rules",androidxTest)
        androidTestImplementation("androidx.test","runner",androidxTest)
        androidTestImplementation("androidx.ui","ui-test",compose)
    }


}
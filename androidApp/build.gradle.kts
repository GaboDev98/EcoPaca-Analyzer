import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
}

// Release signing: credentials live in keystore.properties (gitignored) at the repo root,
// or in env vars on CI. Without them, release builds are produced unsigned.
val keystoreProps = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
fun signingValue(key: String, env: String): String? =
    keystoreProps.getProperty(key) ?: System.getenv(env)

android {
    namespace = "com.gabodev.iotdevicemonitor"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        // Must match the package registered in Google Play Console
        applicationId = "com.gabodev.iotdevicemonitor"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        // Must be higher than any build already uploaded to Play. Override with -PversionCode=N
        versionCode = (project.findProperty("versionCode") as String?)?.toInt() ?: 1
        versionName = "1.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        val storePath = signingValue("storeFile", "ECOPACA_KEYSTORE_PATH")
        if (storePath != null) {
            create("release") {
                storeFile = rootProject.file(storePath)
                storePassword = signingValue("storePassword", "ECOPACA_KEYSTORE_PASSWORD")
                keyAlias = signingValue("keyAlias", "ECOPACA_KEY_ALIAS")
                keyPassword = signingValue("keyPassword", "ECOPACA_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.tooling.preview)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    debugImplementation(libs.compose.ui.tooling)
}

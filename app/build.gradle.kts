plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")
    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        getByName("main") {
            assets.srcDir(rootProject.file("game/assets"))
            jniLibs.srcDir(project.file("libs"))
        }
    }

    packagingOptions {
        exclude("META-INF/robovm/ios/robovm.xml")
    }
}

val gdxVersion = "1.9.10"

val natives by configurations.register("natives")

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":game:core"))

    implementation("androidx.appcompat:appcompat:1.1.0-rc01")
    implementation("androidx.core:core-ktx:1.2.0-alpha03")
    androidTestImplementation("androidx.test:runner:1.3.0-alpha02")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0-alpha02")

    testImplementation("junit:junit:4.12")

    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
    natives("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-armeabi")
    natives("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-x86_64")
}

tasks {
    task("copyAndroidNatives") {
        configurations["natives"].copy().files.forEach {
            val outputDir = when {
                    it.name.endsWith("natives-arm64-v8a.jar") -> file("libs/arm64-v8a")
                    it.name.endsWith("natives-armeabi-v7a.jar") -> file("libs/armeabi-v7a")
                    it.name.endsWith("natives-armeabi.jar") -> file("libs/armeabi")
                    it.name.endsWith("natives-x86_64.jar") -> file("libs/x86_64")
                    it.name.endsWith("natives-x86.jar") -> file("libs/x86")
                    else -> return@forEach
                }
            copy {
                mkdir(outputDir)
                from(zipTree(it))
                into(outputDir)
                include("*.so")
            }
        }
    }

    whenTaskAdded {
        if("package" in name) {
            dependsOn("copyAndroidNatives")
        }
    }
}
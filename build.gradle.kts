// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
allprojects {
    repositories {
        jcenter()
        google()
    }
}

tasks {
    task("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}

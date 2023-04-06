import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "ltd.matrixstudios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.9.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.sparkjava:spark-kotlin:1.0.0-alpha")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.Revxrsal.Lamp:common:3.0.0")
    implementation("com.github.Revxrsal.Lamp:cli:3.0.0")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ltd.matrixstudios.authorize.Authorize")
}
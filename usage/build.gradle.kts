
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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.7.2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ltd.matrixstudios.impl.AuthorizeUsage")
}
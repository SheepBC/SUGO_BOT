import org.apache.tools.ant.taskdefs.Jar

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.0.3"
}

group = "Bc.Sheep_BC"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://maven.lavalink.dev/releases")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("dev.kord:kord-core:0.15.0")
    implementation("dev.kord:kord-voice:0.15.0")
    implementation("dev.kord:kord-core-voice:0.15.0")

    implementation("com.moandjiezana.toml:toml4j:0.7.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    implementation("dev.lavalink.youtube:common:1.11.3")
    implementation("dev.arbjerg:lavaplayer:2.2.3")

    implementation("io.ktor:ktor-client-core:3.1.1")
    implementation("io.ktor:ktor-client-cio:3.1.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.1")

    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.8.0")

}

tasks.test {
    useJUnitPlatform()
}

application{
     mainClass.set("Bc.Sheep_BC.Main.MainKt")
}

kotlin {
    jvmToolchain(21)
}

ktor{
    fatJar {
        archiveFileName.set("SUGO_BOT.jar")
    }
}



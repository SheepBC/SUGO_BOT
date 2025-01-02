plugins {
    kotlin("jvm") version "1.9.23"
}

group = "Bc.Sheep_BC"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("dev.kord:kord-core:0.15.0")
    implementation("dev.kord:kord-voice:0.15.0")
    implementation("dev.kord:kord-core-voice:0.15.0")

    implementation("com.moandjiezana.toml:toml4j:0.7.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
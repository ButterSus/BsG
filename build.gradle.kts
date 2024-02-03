plugins {
    kotlin("jvm") version "1.9.21"
    id("com.moriatsushi.cacheable") version "0.0.3"
}

group = "com.buttersus"
version = "0.0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.moriatsushi.cacheable:cacheable-core:0.0.3")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.3.14")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}
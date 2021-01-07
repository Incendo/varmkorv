plugins {
    kotlin("jvm") version "1.4.21"
}

group = "org.incendo.varmkorv"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.incendo:korvkiosk-api:1.16.4-R0.1-SNAPSHOT")
}

plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "hu.yijun"
version = "0.1"

repositories {
    mavenCentral()
}

val kotlinVersion = "2.0.20"
val flatlafVersion = "3.6"
val assertjVersion = "3.27.3"
val koinVersion = "3.5.0"
val junitVersion = "5.10.2"

dependencies {
    implementation("com.formdev:flatlaf:$flatlafVersion")
    implementation("com.formdev:flatlaf-extras:$flatlafVersion")
    implementation("io.insert-koin:koin-core:$koinVersion")

    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

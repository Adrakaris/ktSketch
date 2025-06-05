plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "hu.yijun"
version = "0.1"

repositories {
    mavenCentral()
}

val flatlafVersion = "3.6"
val assertjVersion = "3.27.3"
val koinVersion = "3.5.0"
val junitVersion = "5.10.2"
val kotlinxVersion = "1.10.2"

dependencies {
    implementation("com.formdev:flatlaf:$flatlafVersion")
    implementation("com.formdev:flatlaf-extras:$flatlafVersion")
    implementation("com.formdev:flatlaf-fonts-inter:4.1")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinxVersion")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$kotlinxVersion")

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

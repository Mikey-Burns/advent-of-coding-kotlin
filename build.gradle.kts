plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.0")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/main/kotlin")
        }
    }

    wrapper {
        gradleVersion = "8.10"
    }
}

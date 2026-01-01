plugins {
    java
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "me.lukiiy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("lib/cb1060.jar"))
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
        minimize()
    }

    jar { enabled = false }

    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf("version" to version)

        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

kotlin.jvmToolchain(8)
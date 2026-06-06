plugins {
    java
}

group = "me.lukiiy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("lib/craftbukkit-1060.jar"))
}

tasks {
    processResources {
        val props = mapOf("version" to version)

        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
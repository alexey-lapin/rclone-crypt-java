pluginManagement {
    plugins {
        id("com.diffplug.spotless") version "5.14.2"
        id("com.github.ben-manes.versions") version "0.39.0"
        id("com.github.johnrengelman.shadow") version "7.0.0"
    }
}

rootProject.name = "pg-cipher"

include("jrclone")
include("jrclone-cli")

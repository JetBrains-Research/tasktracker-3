pluginManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        gradlePluginPortal()
    }
}

rootProject.name = "tasktracker-3"
include("ij-plugin")
include("ij-server")
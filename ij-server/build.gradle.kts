group = rootProject.group
version = rootProject.version

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.ktor.get().pluginId) version libs.versions.ktor.get()
    id("com.google.cloud.tools.jib") version "3.3.1"
}

application {
    mainClass.set("org.jetbrains.research.tasktracker.ApplicationKt")


    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(rootProject.libs.ktor.server.core)
    implementation(rootProject.libs.ktor.server.netty)
    implementation(rootProject.libs.logback)
    implementation(rootProject.libs.postgres)
    implementation(rootProject.libs.exposed.core)
    implementation(rootProject.libs.exposed.dao)
    implementation(rootProject.libs.exposed.jdbc)
    implementation(rootProject.libs.exposed.time)
    testImplementation(rootProject.libs.ktor.server.tests)
    testImplementation(rootProject.libs.h2)
}

jib {
    container {
        ports = listOf("8080")
        mainClass = "org.jetbrains.research.tasktracker.ApplicationKt"

        jvmFlags = listOf(
            "-server",
            "-Djava.awt.headless=true",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+UseCGroupMemoryLimitForHeap",
            "-XX:InitialRAMFraction=2",
            "-XX:MinRAMFraction=2",
            "-XX:MaxRAMFraction=2",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=100",
            "-XX:+UseStringDeduplication"
        )
    }
}

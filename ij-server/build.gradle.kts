import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = rootProject.group
version = rootProject.version

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.ktor.get().pluginId) version libs.versions.ktor.get()
}

val jdkVersion = libs.versions.jdk11.get()

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

tasks{
    withType<JavaCompile> {
        sourceCompatibility = jdkVersion
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    withType<Detekt>().configureEach {
        jvmTarget = jdkVersion
    }
}

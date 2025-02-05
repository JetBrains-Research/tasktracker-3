import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = rootProject.group
version = rootProject.version

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("io.ktor.plugin") version "3.0.3"
}

val jdkVersion = libs.versions.jdk21.get()
val mainClassPath = "org.jetbrains.research.tasktracker.ApplicationKt"


application {
    mainClass.set(mainClassPath)

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
        targetCompatibility = JavaVersion.VERSION_21.toString()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    withType<Detekt>().configureEach {
        jvmTarget = jdkVersion
    }

   withType<Jar> {
        manifest {
            attributes["Main-Class"] = mainClassPath
        }
    }
}

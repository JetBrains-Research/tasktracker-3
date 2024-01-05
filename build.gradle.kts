import io.gitlab.arturbosch.detekt.Detekt

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

@Suppress("DSL_SCOPE_VIOLATION") // "libs" produces a false-positive warning, see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    id(libs.plugins.kotlin.jvm.get().pluginId) version libs.versions.kotlin.get()
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.serialization)
    alias(libs.plugins.changelog)
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

val jdkVersion = libs.versions.jdk.get()

allprojects {
    apply {
        with(rootProject.libs.plugins) {
            listOf(kotlin.jvm, buildconfig, detekt, serialization, changelog).map { it.get().pluginId }.forEach(::plugin)
        }
    }

    repositories {
        mavenCentral()
        maven("https://packages.jetbrains.team/maven/p/ki/maven")
    }

    dependencies {
        implementation(rootProject.libs.csv)
        detektPlugins(rootProject.libs.detekt.formatting)
        implementation(rootProject.libs.joda)
        testImplementation(kotlin("test"))
    }

    tasks {
        withType<JavaCompile> {
            sourceCompatibility = jdkVersion
            targetCompatibility = JavaVersion.VERSION_17.toString()
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        }

        withType<Detekt>().configureEach {
            jvmTarget = jdkVersion
        }

        test {
            useJUnit()
        }
    }

    detekt {
        buildUponDefaultConfig = true
        allRules = false
        config = files("$rootDir/config/detekt.yml")
    }
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }
}

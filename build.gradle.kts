import io.gitlab.arturbosch.detekt.Detekt

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

@Suppress("DSL_SCOPE_VIOLATION") // "libs" produces a false-positive warning, see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    id(libs.plugins.kotlin.jvm.get().pluginId) version libs.versions.kotlin.get()
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.intellij)
    alias(libs.plugins.detekt)
    alias(libs.plugins.serialization)
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
}

val jdkVersion = libs.versions.jdk.get()

dependencies {
    detektPlugins(rootProject.libs.detekt.formatting)
    implementation(rootProject.libs.kaml)
    implementation(rootProject.libs.snakeyaml)
    testImplementation(kotlin("test"))
}

intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    plugins.set(properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) })
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    withType<JavaCompile> {
        sourceCompatibility = jdkVersion
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    withType<Detekt>().configureEach {
        jvmTarget = jdkVersion
    }

    withType<org.jetbrains.intellij.tasks.BuildSearchableOptionsTask>()
        .forEach { it.enabled = false }
}

tasks.test {
    useJUnit()
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("$projectDir/config/detekt.yml")
}

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.markdownToHTML

group = rootProject.group
version = rootProject.version

fun properties(key: String) = providers.gradleProperty(key)

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.intellij)
}

val jdkVersion = libs.versions.jdk17.get()

dependencies {
    implementation(rootProject.libs.kaml)
    implementation(rootProject.libs.snakeyaml)
    implementation(rootProject.libs.kinference)
    implementation(rootProject.libs.javacv)
    implementation(rootProject.libs.ktor.client.cio)
    implementation(rootProject.libs.ktor.client.core)
    implementation(rootProject.libs.ktor.client.json)
    implementation(rootProject.libs.ktor.client.serialization)
    implementation(rootProject.libs.ktor.client.content.negotiation)
    implementation(rootProject.libs.ktor.serialization.kotlinx.json)
    implementation(rootProject.libs.slf4j)
    implementation(rootProject.libs.opencv)
}

intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    plugins.set(properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) })
}

tasks {
    withType<org.jetbrains.intellij.tasks.BuildSearchableOptionsTask>()
        .forEach { it.enabled = false }

    patchPluginXml {
        val description = """
            CodeMood – the revolutionary plugin that understands and affirms your emotions while you code!

            The plugin will ask you permission to record the coding session using one of available video devices.
            _We don't send the photos to a server and handle them locally._
            During the session, you can click on the plugin icon to pop up a dashboard with
            an emoticon reflecting the programmer's current emotion.
            In the end of the coding session you might fill out a short survey about your feelings.

            **Download CodeMood today and start coding with emotions in harmony.**
        """.trimIndent()
        pluginDescription.set(description.run { markdownToHTML(this) })

        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))
    }

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

}

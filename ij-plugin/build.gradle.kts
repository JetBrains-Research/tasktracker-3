import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.incremental.createDirectory

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

val defaultPropertiesDirectory = project.file("src/main/resources/properties/default")
val actualPropertiesDirectory = project.file("src/main/resources/properties/actual").also { if (!it.exists()) it.createDirectory() }

tasks {
    val checkPropertiesExist = register("checkPropertiesExist") {
        doLast {
            defaultPropertiesDirectory.listFiles()?.forEach { file ->
                val newFile = File("${actualPropertiesDirectory.absolutePath}/${file.name}")
                if (!newFile.exists()) {
                    newFile.createNewFile()
                    newFile.writeText(file.readText())
                }
            }
        }
    }

    configureEach {
        if (name != checkPropertiesExist.name) {
            dependsOn(checkPropertiesExist)
        }
    }

    withType<org.jetbrains.intellij.tasks.BuildSearchableOptionsTask>()
        .forEach { it.enabled = false }

    patchPluginXml {
        val description = """
            TaskTracker-3 - a revolutionary plugin for collecting detailed data during education.
            The plugin collects various user activities during interactions with it, such as code
            snapshots with a certain granularity, all interactions with the interface, shortcuts,
            and so on. Extensive configuration options with config files that control its entire
            functionality make it very lightweight and easy to set up and use.
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

group = rootProject.group
version = rootProject.version

fun properties(key: String) = providers.gradleProperty(key)

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.intellij)
}

dependencies{
    implementation(rootProject.libs.kaml)
    implementation(rootProject.libs.snakeyaml)
    implementation(rootProject.libs.csv)
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
}

//configurations.all {
//    exclude("org.slf4j", "slf4j-api")
//}

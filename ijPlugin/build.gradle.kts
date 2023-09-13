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

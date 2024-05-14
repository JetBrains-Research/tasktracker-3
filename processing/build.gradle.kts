import io.gitlab.arturbosch.detekt.Detekt

group = rootProject.group
version = rootProject.version

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.dataframe.get().pluginId) version libs.versions.dataframe.get()
}


val jdkVersion = libs.versions.jdk17.get()

dependencies {
    implementation(rootProject.libs.dataframe)
}

tasks{
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

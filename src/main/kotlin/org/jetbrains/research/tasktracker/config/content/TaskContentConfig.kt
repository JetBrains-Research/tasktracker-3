package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.models.Extension
import java.io.File

// TODO should it be a data class?
@Serializable
class TaskContentConfig(
    val name: String,
    val description: String,
    // TODO input, output
    val languages: List<Extension> = Extension.values().toList()
) : BaseConfig {

    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        fun buildConfig(configFile: File): TaskContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}

package org.jetBrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

// TODO should it be a data class?
@Serializable
class TaskContentConfig : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        fun buildConfig(configFile: File): TaskContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}

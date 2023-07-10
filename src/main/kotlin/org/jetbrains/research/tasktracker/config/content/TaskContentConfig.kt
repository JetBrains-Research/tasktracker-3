package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

// TODO should it be a data class?
@Serializable
class TaskContentConfig : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): TaskContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}

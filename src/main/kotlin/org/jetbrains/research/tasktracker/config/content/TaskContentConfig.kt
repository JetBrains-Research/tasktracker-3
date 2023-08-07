package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

/**
 * A configuration containing all the data necessary for creating and maintaining a task within the plugin.
 * In the description, you can add a link to the file, with the format being '[text](task_id)'.
 */
@Serializable
data class TaskContentConfig(val tasks: List<TaskInfo>) : BaseConfig {

    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        fun buildConfig(configFile: File): TaskContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}

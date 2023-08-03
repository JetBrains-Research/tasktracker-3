package org.jetbrains.research.tasktracker.config.tasksInfo

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.properties.PropertiesController.configRoot
import java.io.File

@Serializable
data class TasksInfoConfig(
    val taskInfos: List<TaskInfo>
) : BaseConfig {

    val configNames: List<String>
        get() = taskInfos.map { it.configDirectory }

    companion object {
        const val TASKS_INFO_CONFIG_NAME = "tasks_info_default.yaml"
        fun buildConfig(): TasksInfoConfig {
            val configFile = File("$configRoot/$TASKS_INFO_CONFIG_NAME")
            return YamlConfigLoadStrategy.load(configFile.readText(), serializer())
        }
    }
}

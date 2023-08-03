package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.tracking.task.TaskFile
import java.io.File

@Serializable
data class TaskContentConfig(
    val name: String,
    val description: String,
    val inputsOutputs: List<InputOutput>? = null,
    val files: List<TaskFileDto> = listOf(TaskFileDto.defaultTaskFileDto),
    val root: String = name,
    val focusFile: String? = null
) : BaseConfig {

    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        fun buildConfig(configFile: File): TaskContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }

    fun getTask(extension: Extension) = object : Task {
        override val name: String
            get() = this@TaskContentConfig.name
        override val description: String
            get() = this@TaskContentConfig.description
        override val taskFiles: List<TaskFile>
            get() = getTaskFiles(extension)
        override val root: String
            get() = this@TaskContentConfig.root
    }

    private fun getTaskFiles(extension: Extension) = files.map { it.toTaskFile(extension, name) }
}

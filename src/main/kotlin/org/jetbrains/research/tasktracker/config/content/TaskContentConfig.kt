package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.task.SourceSet
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.tracking.task.TaskFile
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

    fun getTask(extension: Extension) = object : Task {
        override val name: String
            get() = this@TaskContentConfig.name
        override val description: String
            get() = this@TaskContentConfig.description
        override val taskFiles: List<TaskFile>
            get() = getTaskFiles(extension)
        override val root: String
            get() = name
    }

    // TODO refactor to have not only 'hello world'
    private fun getTaskFiles(extension: Extension) = listOf(
        object : TaskFile {
            override val content: String?
                get() = null
            override val filename: String
                get() = "task"
            override val relativePath: String
                get() = ""
            override val extension: Extension
                get() = extension
            override val sourceSet: SourceSet
                get() = SourceSet.SRC
        }
    )
}

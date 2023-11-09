package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.config.content.task.ProgrammingTask
import org.jetbrains.research.tasktracker.handler.content.TaskContentHandler
import java.io.File

/**
 * A configuration containing all the data necessary for creating and maintaining a task within the plugin.
 * In the description, you can add a link to the file, with the format being '[text](task_id)'.
 */
@Serializable
data class TaskContentConfig(val tasks: List<ProgrammingTask>) : BaseConfig {

    override val configName: String
        get() = "task_content"

    override fun buildHandler() = TaskContentHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        fun buildConfig(configFile: File): TaskContentConfig {
            val config = YamlConfigLoadStrategy.load(configFile.readText(), serializer())
            config.tasks.forEach { t ->
                t.language?.let {
                    t.files.forEach { f ->
                        f.extension = it
                        f.relativePath = "${t.name}/${f.relativePath}"
                        f.content = f.gatherContent()
                    }
                }
            }
            return config
        }
    }
}

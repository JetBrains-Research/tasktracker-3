package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.task.SourceSet

@Serializable
data class TaskInfo(

    /**
     * Task name.
     */
    val name: String,

    /**
     * Task description.
     */
    val description: String,

    /**
     * id that will be used to navigate to the required task in the Scenario.
     */
    val id: String,

    /**
     * represents the input and output pair for the task, or null if such is not implied.
     */
    val inputsOutputs: List<InputOutput>? = null,

    /**
     * Files specified in the configuration or 1 default file.
     */
    val files: List<TaskFileInfo> = listOf(TaskFileInfo.defaultTaskFileDto),

    /**
     * root directory relative to the plugin directory in the project.
     */
    val root: String = name,

    /**
     * The task language, which will be applied to all files
     * if they do not have their own specified.
     */
    val language: Extension,

    /**
     * id of the file that the development environment
     * will focus on when the current task init.
     */
    val focusFileId: String? = null
)

@Serializable
data class TaskFileInfo(val filename: String, val sourceSet: SourceSet) {

    /**
     * Task file language
     */
    val language: Extension? = null

    /**
     * Template name that exists in the `configRoot/content`
     * and will be subsequently loaded into the project.
     */
    val templateFile: String? = null

    /**
     * task file relative path.
     */
    val relativePath: String = ""

    /**
     * id is intended for quick access to the corresponding VirtualFile.
     */
    val id: String? = null

    companion object {
        val defaultTaskFileDto = TaskFileInfo("task", SourceSet.SRC)
    }
}

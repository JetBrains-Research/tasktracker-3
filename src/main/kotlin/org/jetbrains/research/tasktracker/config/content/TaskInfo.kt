package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.task.SourceSet

@Serializable
data class TaskInfo(
    val name: String,
    val description: String,
    val id: String,
    val inputsOutputs: List<InputOutput>? = null,
    val files: List<TaskFileInfo> = listOf(TaskFileInfo.defaultTaskFileDto),
    val root: String = name,
    val language: Extension,
    val focusFileId: String? = null
)

@Serializable
data class TaskFileInfo(val filename: String, val sourceSet: SourceSet) {
    val language: Extension? = null
    val templateFile: String? = null
    val relativePath: String = ""
    val id: String? = null

    companion object {
        val defaultTaskFileDto = TaskFileInfo("task", SourceSet.SRC)
    }
}

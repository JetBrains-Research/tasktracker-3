package org.jetbrains.research.tasktracker.config.tasksInfo

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.models.Extension

@Serializable
data class TaskInfo(
    val name: String,
    val configDirectory: String,
    val languages: List<Extension> = Extension.values().toList()
)

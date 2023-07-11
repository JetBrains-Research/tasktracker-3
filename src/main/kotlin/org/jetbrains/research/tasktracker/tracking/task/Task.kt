package org.jetbrains.research.tasktracker.tracking.task

import org.jetbrains.research.tasktracker.models.Extension

interface Task {
    val content: String?
    val filename: String
    val relativeFilePath: String?
    val extension: Extension
}

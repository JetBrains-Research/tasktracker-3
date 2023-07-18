package org.jetbrains.research.tasktracker.tracking.task

import org.jetbrains.research.tasktracker.models.Extension

interface TaskFile {
    val content: String?
    val filename: String
    val relativePath: String
    val extension: Extension
    val sourceSet: SourceSet
}

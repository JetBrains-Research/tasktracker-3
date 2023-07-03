package org.jetBrains.research.tasktracker.tracking.task

import org.jetBrains.research.tasktracker.models.Extension

interface Task {
    fun getContent(): String?
    fun getRelativeFilePath(): String?
    fun getFileName(): String
    fun getExtension(): Extension
}

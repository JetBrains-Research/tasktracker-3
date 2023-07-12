package org.jetbrains.research.tasktracker.tracking.task

import org.jetbrains.research.tasktracker.models.Extension

interface Task {
    fun getContent(): String?
    fun getRelativeFilePath(): String?
    fun getFileName(): String
    fun getExtension(): Extension
}

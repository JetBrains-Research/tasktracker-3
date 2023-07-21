package org.jetbrains.research.tasktracker.tracking.task

interface Task {
    val name: String
    val taskFiles: List<TaskFile>
    val root: String
}

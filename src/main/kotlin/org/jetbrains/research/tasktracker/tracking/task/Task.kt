package org.jetbrains.research.tasktracker.tracking.task

interface Task {
    /**
     * @return The name of the task as a string.
     */
    val name: String

    /**
     * @return The description of the task.
     */
    val description: String

    /**
     * @return Task files of the task.
     */
    val taskFiles: List<TaskFile>

    /**
     * @return root directory relative to the plugin directory in the project.
     */
    val root: String

    /**
     * @return id of the file that the development environment
     * will focus on when the current task init.
     */
    val focusFileId: String?
}

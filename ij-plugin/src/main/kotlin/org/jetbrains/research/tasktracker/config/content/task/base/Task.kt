package org.jetbrains.research.tasktracker.config.content.task.base

interface Task {
    /**
     * id that will be used to navigate to the required task in the Scenario.
     */
    val id: String

    /**
     * @return The name of the task as a string.
     */
    val name: String

    /**
     * @return The description of the task.
     */
    val description: String
}

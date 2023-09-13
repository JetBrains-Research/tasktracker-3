package org.jetbrains.research.tasktracker.ui.main.panel.storage

import org.jetbrains.research.tasktracker.config.content.task.base.Task

/**
 * Stores all necessary data to build the main UI panel quickly
 */
object MainPanelStorage {
    var taskIdTask: MutableMap<String, Task> = mutableMapOf()
    var currentResearchId: Int? = null
    var userId: Int? = null
}

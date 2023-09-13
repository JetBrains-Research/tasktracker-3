package org.jetbrains.research.tasktracker.ui.main.panel.storage

import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamInfo

/**
 * Stores all necessary data to build the main UI panel quickly
 */
object MainPanelStorage {
    var taskIdTask: MutableMap<String, Task> = mutableMapOf()
    val camerasInfo: MutableList<WebCamInfo> = mutableListOf()
    var currentDeviceNumber: Int? = null
    var currentResearchId: Int? = null
    var userId: Int? = null
}

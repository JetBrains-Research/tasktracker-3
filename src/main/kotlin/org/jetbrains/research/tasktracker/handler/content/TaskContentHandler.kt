package org.jetbrains.research.tasktracker.handler.content

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage

// TODO: add hierarchy for different types of tasks
class TaskContentHandler(override val config: TaskContentConfig) : BaseHandler {

    override fun setup(project: Project) {
        MainPanelStorage.taskIdTask = config.tasks.associateBy { it.id }.toMutableMap()
    }
}

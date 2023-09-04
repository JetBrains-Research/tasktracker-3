package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class IdeHandler(override val config: MainIdeConfig) : BaseHandler {
    private val childHandlers: List<BaseHandler>

    init {
        with(config) {
            childHandlers = listOfNotNull(inspectionConfig).map { it.buildHandler() }
        }
    }

    override fun setup(project: Project) {
        childHandlers.forEach {
            it.setup(project)
        }
    }
}

package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler

class IdeHandler(override val config: MainIdeConfig, override val project: Project) : BaseProjectHandler {
    private val childHandlers: List<BaseProjectHandler?>

    init {
        with(config) {
            childHandlers = listOfNotNull(inspectionConfig, settingsConfig).map { it.buildHandler(project) }
        }
    }

    override fun setup() {
        childHandlers.forEach {
            it?.setup()
        }
    }

    override fun destroy() {
        // TODO destroy
    }
}

package org.jetbrains.research.tasktracker.handler.init

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class InitializationHandler(private val mainConfig: MainTaskTrackerConfig) {
    // TODO maybe store handlers by type?
    private val handlers = mutableListOf<BaseHandler>()

    // Send data to the server/file, remove extra listeners, delete files etc
    private fun destroyHandlers() {
        handlers.forEach { it.destroy() }
        handlers.clear()
    }

    fun setupContentOnly() {
        destroyHandlers()
        TODO("Call extra content handler to get a test/intro/something else and show to the user")
    }

    fun setupEnvironment(project: Project) {
        destroyHandlers()

        handlers.addAll(
            mainConfig.getAllConfigs().mapNotNull {
                when (it) {
                    is BaseProjectConfig -> it.buildHandler(project)
                    is BaseConfig -> it.buildHandler()
                    else -> null
                }
            }
        )
        handlers.forEach { it.setup() }
    }
}

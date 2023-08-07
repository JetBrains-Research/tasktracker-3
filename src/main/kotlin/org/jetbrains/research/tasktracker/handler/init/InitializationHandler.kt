package org.jetbrains.research.tasktracker.handler.init

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.content.TaskContentHandler
import org.jetbrains.research.tasktracker.handler.ide.IdeHandler
import org.jetbrains.research.tasktracker.handler.tracking.CodeTrackingHandler

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

    fun setupEnvironment() {
        destroyHandlers()

        handlers.addAll(
            listOfNotNull(
                mainConfig.mainIdeConfig?.let { IdeHandler(it) },
                mainConfig.codeTrackingConfig?.let { CodeTrackingHandler(it) },
                mainConfig.taskContentConfig?.let { TaskContentHandler(it) }
                // TODO: add others
            )
        )
        handlers.forEach { it.setup() }
    }
}

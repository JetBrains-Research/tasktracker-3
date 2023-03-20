package org.jetBrains.research.tasktracker.handler.init

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.handler.BaseHandler
import org.jetBrains.research.tasktracker.handler.ide.IdeHandler
import org.jetBrains.research.tasktracker.handler.tracking.CodeTrackingHandler

class InitializationHandler(private val mainConfig: MainTaskTrackerConfig) {
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
                mainConfig.codeTrackingConfig?.let { CodeTrackingHandler(it) }
                // TODO: add others
            )
        )
        handlers.forEach { it.setup() }
    }
}

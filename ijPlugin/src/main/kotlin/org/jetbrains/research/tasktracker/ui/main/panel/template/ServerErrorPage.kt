package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin

class ServerErrorPage(private val userMessage: String = defaultText) : ErrorPageTemplate() {
    override val title: String
        get() = "Oops, it looks like there's been a hiccup with our server."
    override val description: String
        get() = userMessage

    companion object {
        val config = TaskTrackerPlugin.mainConfig.serverErrorPageConfig
            ?: error("serverErrorPageConfig has not initialized yet!")
        private val defaultText = config.defaultMessage
    }
}

package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin

class ServerErrorPageTemplate(private val userMessage: String = defaultText) : HtmlBaseFileTemplate() {
override val contentFilename: String
    get() = "serverError"

    override val arguments: Array<String>
        get() = arrayOf(userMessage)

    companion object {
        val config = TaskTrackerPlugin.mainConfig.serverErrorPageConfig
            ?: error("serverErrorPageConfig has not initialized yet!")
        private val defaultText = config.defaultMessage
    }
}

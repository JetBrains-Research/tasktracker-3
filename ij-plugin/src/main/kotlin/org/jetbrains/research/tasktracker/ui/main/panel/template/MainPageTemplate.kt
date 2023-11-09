package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin

class MainPageTemplate(private val pluginName: String, private val pluginDescription: String) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "main"

    override val arguments: Array<String>
        get() = arrayOf(pluginName, pluginDescription)

    companion object {
        fun loadCurrentTemplate(): MainPageTemplate {
            val config = TaskTrackerPlugin.mainConfig.mainPageConfig
                ?: error("mainPageConfig has not initialized yet!")
            return MainPageTemplate(config.pluginName, config.pluginDescription)
        }
    }
}

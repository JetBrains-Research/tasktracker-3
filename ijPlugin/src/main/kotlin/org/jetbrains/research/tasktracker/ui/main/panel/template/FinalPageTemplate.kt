package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin

class FinalPageTemplate(private val text: String) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "final"

    override val arguments: Array<String>
        get() = arrayOf(text)

    companion object {
        fun loadCurrentTemplate(): FinalPageTemplate {
            val config = TaskTrackerPlugin.mainConfig.finalPageConfig
                ?: error("finalPageConfig has not initialized yet!")
            return FinalPageTemplate(config.text)
        }
    }
}

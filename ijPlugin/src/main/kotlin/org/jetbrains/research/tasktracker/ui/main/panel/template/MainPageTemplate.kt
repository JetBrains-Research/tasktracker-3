package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin

class MainPageTemplate(private val pluginName: String, private val pluginDescription: String) : HtmlBaseTemplate() {
    override val content: String
        get() = buildPageTemplate()

    private fun buildPageTemplate() = """
        <div class="container">
            <p class="big-font">$pluginName</p>
            <br>
            ${pluginDescription.wrapToSmallText()}
        </div>
    """.trimIndent()

    companion object {
        fun loadCurrentTemplate(): MainPageTemplate {
            requireNotNull(TaskTrackerPlugin.mainConfig.mainPageConfig) { "mainPageConfig has not initialized yet!" }
            val config = TaskTrackerPlugin.mainConfig.mainPageConfig ?: error("TODO")
            return MainPageTemplate(config.pluginName, config.pluginDescription)
        }
    }
}

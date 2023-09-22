package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

class MainPageTemplate(private val pluginName: String, private val pluginDescription: String) : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "main"

    override fun pageContent(theme: Theme, vararg arguments: String) =
        super.pageContent(theme, *arguments, pluginName, pluginDescription)

//    private fun buildPageTemplate() = """
//        <div class="container">
//            <p class="big-font">$pluginName</p>
//            <br>
//            ${pluginDescription.wrapToSmallText()}
//        </div>
//    """.trimIndent()

    companion object {
        fun loadCurrentTemplate(): MainPageTemplate {
            requireNotNull(TaskTrackerPlugin.mainConfig.mainPageConfig) { "mainPageConfig has not initialized yet!" }
            val config = TaskTrackerPlugin.mainConfig.mainPageConfig ?: error("TODO")
            return MainPageTemplate(config.pluginName, config.pluginDescription)
        }
    }
}

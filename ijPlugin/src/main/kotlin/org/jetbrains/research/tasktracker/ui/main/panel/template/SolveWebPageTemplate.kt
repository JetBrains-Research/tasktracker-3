package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.content.SolveWebPageConfig
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme
import org.jetbrains.research.tasktracker.ui.main.panel.template.ErrorPageTemplate.wrapToSmallText

class SolveWebPageTemplate(private val config: SolveWebPageConfig) : HtmlTemplateBase() {
    override val htmlFileName: String? = null

    override fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme, buildPageTemplate())

    private fun buildPageTemplate() = """
        <div class="container">
            <p class="big-font">Task</p>
            <br>
            <p class="small-font">${config.text.wrapToSmallText()}</p>
        </div>
    """.trimIndent()

    companion object {
        fun loadCurrentTemplate(): SolveWebPageTemplate {
            requireNotNull(TaskTrackerPlugin.mainConfig.solveWebPageConfig) {
                "solveWebConfig has not initialized yet!"
            }
            val config = TaskTrackerPlugin.mainConfig.solveWebPageConfig ?: error("TODO")
            return SolveWebPageTemplate(config)
        }
    }
}

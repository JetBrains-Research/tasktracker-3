package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.survey.SurveyConfig
import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginPanelFactory
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage

class SurveyTemplate(val config: SurveyConfig) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "survey"

    override val cssFilename: String
        get() = "survey"

    override val arguments: Array<String>
        get() = arrayOf(request(), config.toHtml())

    private fun request() = "${MainPluginPanelFactory.DOMAIN}/confirm-survey?id=${MainPanelStorage.currentResearchId}"

    companion object {
        fun loadCurrentTemplate(): SurveyTemplate {
            val config = TaskTrackerPlugin.mainConfig.surveyConfig ?: error("surveyConfig has not initialized yet!")
            return SurveyTemplate(config)
        }
    }
}

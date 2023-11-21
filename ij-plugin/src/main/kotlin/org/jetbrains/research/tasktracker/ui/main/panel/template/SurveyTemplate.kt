package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.survey.Survey
import org.jetbrains.research.tasktracker.requests.FileRequests
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage

class SurveyTemplate(val survey: Survey) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "survey"

    override val cssFilename: String
        get() = "survey"

    override val arguments: Array<String>
        get() = arrayOf(request(), survey.toHtml())

    private fun request() = "${FileRequests.DOMAIN}/confirm-survey?id=${MainPanelStorage.currentResearchId}"
}

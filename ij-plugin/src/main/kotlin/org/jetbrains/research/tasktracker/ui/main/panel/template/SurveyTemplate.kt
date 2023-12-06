package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.survey.Survey

class SurveyTemplate(val survey: Survey) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "survey"

    override val cssFilename: String
        get() = "survey"

    override val arguments: Array<String>
        get() = arrayOf(request(), survey.toHtml())

    @Suppress("FunctionOnlyReturningConstant")
    private fun request() = "" // TODO remove request from survey html
}

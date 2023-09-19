package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginPanelFactory
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage

object SurveyTemplate : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "survey"

    override val arguments: Array<String>
        get() = arrayOf(request())

    private fun request() = "${MainPluginPanelFactory.DOMAIN}/confirm-survey?id=${MainPanelStorage.currentResearchId}"
}

package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginPanelFactory
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage

object SurveyTemplate : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "survey"

    override fun pageContent(theme: Theme, vararg arguments: String): String {
        return super.pageContent(theme, *arguments, request())
    }

    private fun request() = "${MainPluginPanelFactory.DOMAIN}/confirm-survey?id=${MainPanelStorage.currentResearchId}"
}

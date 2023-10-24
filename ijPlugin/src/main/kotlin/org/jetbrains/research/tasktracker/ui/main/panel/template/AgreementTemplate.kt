package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.agreement.Agreement

class AgreementTemplate(private val agreements: List<Agreement>) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "agreement"

    override val arguments: Array<String>
        get() = arrayOf(agreementsToHtml())

    private fun agreementsToHtml() = agreements.mapIndexed { index, element ->
        """<p><input id="$index" type="checkbox" name="$index" /> ${element.text}</p>"""
    }.joinToString("")

    companion object {
        fun loadCurrentTemplate(): AgreementTemplate {
            val config = TaskTrackerPlugin.mainConfig.agreementConfig
                ?: error("agreementConfig has not initialized yet!")
            return AgreementTemplate(config.agreements)
        }
    }
}

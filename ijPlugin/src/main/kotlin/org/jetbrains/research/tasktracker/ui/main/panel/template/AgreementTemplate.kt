package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.agreement.Agreement

class AgreementTemplate(private val agreements: List<Agreement>) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "agreement"

    override val arguments: Array<String>
        get() = arrayOf(agreementsToHtml())

    override val cssFilename: String
        get() = "agreement"

    private fun agreementsToHtml() = agreements.mapIndexed { index, element ->
        buildString {
            append("""<div><input id="$index" type="checkbox" name="$index" """)
            append("""${if (element.required) "required" else ""}>""")
            append("""<label for="$index">${element.text.withBr()}</label></div>""")
        }
    }.joinToString("<br>")

    private fun String.withBr() = replace(System.lineSeparator(), "<br>")

    companion object {
        fun loadCurrentTemplate(): AgreementTemplate {
            val config = TaskTrackerPlugin.mainConfig.agreementConfig
                ?: error("agreementConfig has not initialized yet!")
            return AgreementTemplate(config.agreements)
        }
    }
}

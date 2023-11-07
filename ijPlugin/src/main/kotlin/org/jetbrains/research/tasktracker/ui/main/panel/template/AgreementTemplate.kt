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
            append("""<label for="$index">${element.toHtml()}</label></div>""")
        }
    }.joinToString("<br>")

    private fun Agreement.toHtml(): String {
        var html = text.replace(System.lineSeparator(), "<br>")
        if (openLinkInDefaultBrowser) {
            html = html.replace(HREF_PATTERN) { matchResult ->
                val href = matchResult.groupValues[1]
                """<a class="defaultBrowser" href="$href">"""
            }
        }
        return html
    }

    companion object {
        val HREF_PATTERN = """<a href="([^"]*)">""".toRegex()

        fun loadCurrentTemplate(): AgreementTemplate {
            val config = TaskTrackerPlugin.mainConfig.agreementConfig
                ?: error("agreementConfig has not initialized yet!")
            return AgreementTemplate(config.agreements)
        }
    }
}

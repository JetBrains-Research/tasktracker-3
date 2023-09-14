package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

// TODO: add conf
class FinalPageTemplate : HtmlTemplateBase() {
    override val htmlFileName: String? = null

    override fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme, buildPageTemplate())

    private fun buildPageTemplate() = """
        <div class="container">
            <p class="big-font">Thank you for taking part in our emotional journey!</p>
            <br>
            ${THANK_YOU_TEXT.wrapToSmallText()}
        </div>
    """.trimIndent()

    companion object {
        private const val THANK_YOU_TEXT = """
            We’ve turned off all tracking.
            If you wish, you can remove the plugin at any time from the plugins section in the IDE settings.
        """
    }
}

package org.jetbrains.research.tasktracker.ui.main.panel.template

// TODO: add conf
class FinalPageTemplate : HtmlBaseTemplate() {
    override val content: String
        get() = buildPageTemplate()

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

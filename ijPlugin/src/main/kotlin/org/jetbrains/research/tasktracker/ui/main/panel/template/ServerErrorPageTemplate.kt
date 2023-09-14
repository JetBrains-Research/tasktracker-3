package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

// TODO: add a config with userMessage instead of DEFAULT_TEXT
class ServerErrorPageTemplate(private val userMessage: String = defaultText) : HtmlTemplateBase() {
    override val htmlFileName: String? = null

    override fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme, buildPageTemplate())

    private fun buildPageTemplate() = """
        <div class="container">
            <p class="big-font">Oops, it looks like there's been a hiccup with our server.</p>
            <br>
            ${userMessage.wrapToSmallText()}
        </div>
    """.trimIndent()

    companion object {
        private val defaultText = """
            No worries, though! You can still send us your tracked data manually. Here's what to do:
            
            Copy this link: <i>${MainTaskTrackerConfig.pluginFolderPath}</i>
            Open the folder using the link.
            Once you're in the folder, please send us all its files.
            
            We appreciate your assistance!
        """
    }
}
package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

// TODO: add a config with userMessage instead of DEFAULT_TEXT
class ServerErrorPageTemplate(private val userMessage: String = DEFAULT_TEXT) : HtmlTemplateBase() {
    override val htmlFileName: String? = null

    override fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme, buildPageTemplate())

    private fun buildPageTemplate() = """
        <div class="container">
            <p class="big-font">Oooops</p>
            <br>
            ${userMessage.wrapToSmallText()}
            ${pluginFilesLocation().wrapToSmallText()}
        </div>
    """.trimIndent()

    private fun pluginFilesLocation() = """
        Copy this link <i>${MainTaskTrackerConfig.pluginFolderPath}</i>, open the folder and send all files from this folder.
    """.trimIndent()

    companion object {
        private const val DEFAULT_TEXT = """
            Something went wrong with our server. 
            
            Please, send all tracked data manually:
        """
    }
}

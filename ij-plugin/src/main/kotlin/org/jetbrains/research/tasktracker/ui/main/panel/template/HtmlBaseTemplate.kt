package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

/**
 * Presents Base html pages with base template structure
 */
// TODO add example
abstract class HtmlBaseTemplate : HtmlTemplate {

    private val templateContent: String
        get() = getFileContentFromResources(TEMPLATE_FILENAME)

    protected open val css: String? = null

    protected abstract val content: String

    final override val htmlContent: String
        get() = getContentWithTheme(Theme.currentIdeTheme())

    fun getContentWithTheme(theme: Theme): String =
        templateContent.format(loadCss(theme), content)

    private fun loadCss(theme: Theme) = listOfNotNull(
        loadCssFromFile(BASE_CSS_FILE),
        loadCssFromFile(theme.cssFileName),
        css
    ).joinToString(System.lineSeparator())

    protected fun loadCssFromFile(filename: String) = getFileContentFromResources("$CSS_FOLDER/$filename.css")

    protected fun getFileContentFromResources(path: String) =
        HtmlBaseTemplate::class.java.getResource(path)?.readText()
            ?: error("Base file with path `$path` must exist")

    protected fun String.wrapToSmallText() = lines().wrapToSmallText()

    private fun List<String>.wrapToSmallText() = joinToString(System.lineSeparator()) {
        """
            <p class="small-font">$it</p>
        """.trimIndent()
    }

    companion object {
        const val CSS_FOLDER = "css"
        const val BASE_CSS_FILE = "base"
        private const val TEMPLATE_FILENAME = "template.html"
    }
}

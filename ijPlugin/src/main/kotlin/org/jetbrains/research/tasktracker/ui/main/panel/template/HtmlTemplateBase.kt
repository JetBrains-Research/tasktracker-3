package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

sealed class HtmlTemplateBase {
    protected abstract val htmlFileName: String?

    open fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme).format(*arguments)

    private fun loadCss(filename: String) =
        HtmlTemplateBase::class.java.getResource("css/$filename.css")?.readText()?.replace("%", "%%")
            ?: error("Cannot find $filename.css")

    protected fun String.wrapToSmallText() = lines().joinToString(System.lineSeparator()) {
        """
            <p class="small-font">$it</p>
        """.trimIndent()
    }

    private fun cssContent(theme: Theme) = loadCss(theme.cssFileName)

    private fun baseCss() = loadCss("base")

    private fun pageTemplate(theme: Theme): String {
        val currentTemplate = HtmlTemplateBase::class.java.getResource("$htmlFileName.html")?.readText()
            ?: error("Cannot find default page with name $htmlFileName")
        return pageTemplate(theme, currentTemplate)
    }

    protected fun pageTemplate(theme: Theme, template: String) =
        HtmlTemplateBase::class.java.getResource("template.html")?.readText()?.format(
            baseCss(),
            cssContent(theme),
            template
        ) ?: error("Cannot find page template")
}

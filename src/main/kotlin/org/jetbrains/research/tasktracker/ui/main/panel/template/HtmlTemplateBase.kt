package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

sealed class HtmlTemplateBase {
    protected abstract val htmlFileName: String

    fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme).format(*arguments)

    private fun loadCss(filename: String) =
        HtmlTemplateBase::class.java.getResource("css/$filename.css")?.readText()?.replace("%", "%%")
            ?: error("Cannot find $filename.css")

    private fun cssContent(theme: Theme) = loadCss(theme.cssFileName)

    private fun baseCss() = loadCss("base")

    private fun pageTemplate(theme: Theme) = HtmlTemplateBase::class.java
        .getResource("template.html")?.readText()?.format(
            baseCss(),
            cssContent(theme),
            HtmlTemplateBase::class.java.getResource("$htmlFileName.html")?.readText()
                ?: error("Cannot find default page with name $htmlFileName")
        )
        ?: error("Cannot find page template")
}

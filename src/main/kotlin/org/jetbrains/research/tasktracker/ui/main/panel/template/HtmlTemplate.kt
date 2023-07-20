package org.jetbrains.research.tasktracker.ui.main.panel.template

import com.intellij.util.ui.UIUtil

sealed class HtmlTemplate {

    protected abstract val css: CSS
    protected abstract val name: String

    fun pageContent(vararg arguments: String) = pageTemplate().format(*arguments)

    private fun loadCss(filename: String) =
        HtmlTemplate::class.java.getResource("css/$filename.css")?.readText()?.replace("%", "%%")
            ?: error("Cannot find $filename.css")

    private fun cssContent() = loadCss(css.filename)

    private fun baseCss() = loadCss("base")

    private fun pageTemplate() = HtmlTemplate::class.java.getResource("template.html")?.readText()?.format(
        baseCss(),
        cssContent(),
        HtmlTemplate::class.java.getResource("$name.html")?.readText()
            ?: error("Cannot find default page with name $name")
    )
        ?: error("Cannot find page template")

    protected enum class CSS {
        DEFAULT {
            override val filename: String
                get() = if (UIUtil.isUnderDarcula()) "dark" else "light"
        };

        abstract val filename: String
    }
}

object ErrorPageTemplate : HtmlTemplate() {
    override val css: CSS
        get() = CSS.DEFAULT

    override val name: String
        get() = "error"
}

object IndexPageTemplate : HtmlTemplate() {
    override val css: CSS
        get() = CSS.DEFAULT

    override val name: String
        get() = "index"
}

object TasksPageTemplate : HtmlTemplate() {
    override val css: CSS
        get() = CSS.DEFAULT
    override val name: String
        get() = "tasks"
}

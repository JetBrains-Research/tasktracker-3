package org.jetbrains.research.tasktracker.ui.main.panel.template

sealed class HtmlTemplate {

    protected abstract val css: CSS
    protected abstract val name: String

    fun pageContent(vararg arguments: String) = pageTemplate().format(*arguments)

    private fun cssContent() =
        HtmlTemplate::class.java.getResource("css/${css.filename}.css")?.readText()?.replace("%", "%%")
            ?: error("Cannot find ${css.filename}.css")

    private fun pageTemplate() = HtmlTemplate::class.java.getResource("template.html")?.readText()?.format(
        cssContent(),
        HtmlTemplate::class.java.getResource("$name.html")?.readText()
            ?: error("Cannot find default page with name $name")
    )
        ?: error("Cannot find page template")

    protected enum class CSS(val filename: String) {
        DEFAULT("default")
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

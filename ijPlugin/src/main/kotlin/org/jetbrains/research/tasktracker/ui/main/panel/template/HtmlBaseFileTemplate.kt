package org.jetbrains.research.tasktracker.ui.main.panel.template

/**
 * This is a template of a base HTML page, the content of which will be filled from resource files.
 */
sealed class HtmlBaseFileTemplate : HtmlBaseTemplate() {
    protected abstract val contentFilename: String
    protected open val cssFilename: String? = null
    protected open val arguments: Array<String> = emptyArray()

    final override val content: String
        get() = getFileContentFromResources("$contentFilename.html").format(arguments)

    final override val css: String?
        get() = cssFilename?.let { loadCssFromFile(it) }
}

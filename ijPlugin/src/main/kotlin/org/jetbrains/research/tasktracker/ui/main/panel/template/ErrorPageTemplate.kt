package org.jetbrains.research.tasktracker.ui.main.panel.template

abstract class ErrorPageTemplate : HtmlBaseFileTemplate() {
    abstract val title: String
    abstract val description: String

    final override val contentFilename: String
        get() = "error"

    final override val arguments: Array<String>
        get() = arrayOf(title, description)
}

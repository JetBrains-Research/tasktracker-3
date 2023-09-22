package org.jetbrains.research.tasktracker.ui.main.panel.template

class ErrorPageTemplate(
    private val errorCode: String,
    private val errorText: String,
    private val failedUrl: String
) :
    HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "error"
    override val arguments: Array<String>
        get() = arrayOf(errorCode, errorText, failedUrl)
}

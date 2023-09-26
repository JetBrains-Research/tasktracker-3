package org.jetbrains.research.tasktracker.ui.main.panel.template

class DefaultErrorPage(
    private val errorCode: String,
    private val errorText: String,
    private val failedUrl: String
) :
    ErrorPageTemplate() {
    override val title: String
        get() = "Something went wrong"
    override val description: String
        get() = listOf(
            "error code: $errorCode",
            "error text: $errorText",
            "failed url: $failedUrl"
        ).joinToString("<br>")
}

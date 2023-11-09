package org.jetbrains.research.tasktracker.ui.main.panel.template

import java.io.File

class HtmlFileTemplate(file: File, arguments: Array<String> = emptyArray()) : HtmlTemplate {
    override val htmlContent: String = file.readText().format(args = arguments)
}

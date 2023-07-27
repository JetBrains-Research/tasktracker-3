package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

class SolvePageTemplate(val task: Task) : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "solve"

    override fun pageContent(theme: Theme, vararg arguments: String): String {
        return super.pageContent(theme, *arguments, task.name, task.description)
    }
}

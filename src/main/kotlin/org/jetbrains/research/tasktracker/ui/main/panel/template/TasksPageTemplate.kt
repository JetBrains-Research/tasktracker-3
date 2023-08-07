package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

class TasksPageTemplate(private val tasks: List<Task>) : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "tasks"

    override fun pageContent(theme: Theme, vararg arguments: String): String {
        return super.pageContent(theme, *arguments, optionTags())
    }

    // TODO: change to select by id
    private fun optionTags() =
        tasks.joinToString(System.lineSeparator()) { "<option value=\"${it.name}\">${it.name}</option>" }
}

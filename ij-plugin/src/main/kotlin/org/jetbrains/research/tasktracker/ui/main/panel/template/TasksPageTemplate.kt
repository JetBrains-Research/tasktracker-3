package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.content.task.base.Task

class TasksPageTemplate(private val tasks: List<Task>) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "tasks"
    override val arguments: Array<String>
        get() = arrayOf(optionTags())

    private fun optionTags() =
        tasks.joinToString(System.lineSeparator()) { "<option value=\"${it.id}\">${it.name}</option>" }
}

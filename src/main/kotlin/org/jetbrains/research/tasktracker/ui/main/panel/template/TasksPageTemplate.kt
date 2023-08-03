package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.tasksInfo.TaskInfo
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme
import java.util.*

class TasksPageTemplate(private val taskInfos: List<TaskInfo>) : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "tasks"

    override fun pageContent(theme: Theme, vararg arguments: String): String {
        return super.pageContent(theme, *arguments, optionTags(), loadJs())
    }

    private fun optionTags() =
        taskInfos.joinToString(System.lineSeparator()) { "<option value=\"${it.configDirectory}\">${it.name}</option>" }

    private fun taskLanguages() =
        taskInfos.joinToString(
            separator = ",",
            prefix = "{",
            postfix = "}"
        ) { info ->
            "\"${info.name}\": ${info.languages.map { "\"${it.name.lowercase(Locale.getDefault())}\"" }}"
        }

    private fun loadJs() = TasksPageTemplate::class.java.getResource("js/tasks.js")?.readText()?.format(taskLanguages())
        ?: error("Cannot find tasks.js")
}

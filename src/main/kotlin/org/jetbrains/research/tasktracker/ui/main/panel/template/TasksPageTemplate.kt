package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme
import java.util.Locale

class TasksPageTemplate(private val taskConfigs: List<TaskContentConfig>) : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "tasks"

    override fun pageContent(theme: Theme, vararg arguments: String): String {
        return super.pageContent(theme, *arguments, optionTags(), loadJs()).also { println(it) }
    }

    private fun optionTags() =
        taskConfigs.joinToString(System.lineSeparator()) { "<option value=\"${it.name}\">${it.name}</option>" }

    private fun taskLanguages() =
        taskConfigs.joinToString(
            separator = ",",
            prefix = "{",
            postfix = "}"
        ) { config ->
            "\"${config.name}\": ${config.languages.map { "\"${it.name.lowercase(Locale.getDefault())}\"" }}"
        }

    private fun loadJs() = TasksPageTemplate::class.java.getResource("js/tasks.js")?.readText()?.format(taskLanguages())
        ?: error("Cannot find tasks.js")
}

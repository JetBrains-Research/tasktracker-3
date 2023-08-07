package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

class SolvePageTemplate(val task: Task) : HtmlTemplateBase() {
    override val htmlFileName: String
        get() = "solve"

    override fun pageContent(theme: Theme, vararg arguments: String): String {
        return super.pageContent(theme, *arguments, task.name, task.description).reformFileLinks()
    }

    private fun String.reformFileLinks(): String {
        return FILE_PATTERN.replace(this) {
            val matchText = it.groupValues[1]
            val matchLink = it.groupValues[2]
            """<a href="$matchLink" class="file" data-value="$matchLink">$matchText</a>"""
        }
    }

    companion object {
        val FILE_PATTERN = "\\[(.*?)]\\((.*?)\\)".toRegex()
    }
}

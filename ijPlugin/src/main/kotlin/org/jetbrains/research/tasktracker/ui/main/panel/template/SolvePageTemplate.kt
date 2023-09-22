package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.config.content.task.base.Task

class SolvePageTemplate(val task: Task) : HtmlBaseFileTemplate() {
    override val contentFilename: String
        get() = "solve"
    override val arguments: Array<String>
        get() = arrayOf(task.name, task.description.reformFileLinks())

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

package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowAction
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowData
import org.joda.time.DateTime

class ToolWindowLogger(val project: Project) : BaseLogger() {
    override val logPrinter: LogPrinter = initLogPrinter("tool_window_${project.hashCode()}_${project.name}")
        .also {
            it.csvPrinter.printRecord(ToolWindowLoggedData.headers)
        }

    fun log(
        windowId: String,
        action: ToolWindowAction,
        date: DateTime = DateTime.now()
    ) = log(ToolWindowLoggedData.getData(ToolWindowData(date, action, windowId)))
}

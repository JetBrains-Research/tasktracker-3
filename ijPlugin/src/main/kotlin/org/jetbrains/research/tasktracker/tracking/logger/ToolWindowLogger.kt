package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowAction
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowData
import org.joda.time.DateTime

class ToolWindowLogger(val project: Project) : BaseLogger() {
    override val logPrinterFilename: String = "tool_window_${project.hashCode()}_${project.name}"
    override val loggedData: LoggedData<*, *>
        get() = ToolWindowLoggedData

    fun log(
        windowId: String,
        action: ToolWindowAction,
        date: DateTime = DateTime.now()
    ) = log(ToolWindowLoggedData.getData(ToolWindowData(date, action, windowId)))
}

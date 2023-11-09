package org.jetbrains.research.tasktracker.tracking.toolWindow

import org.joda.time.DateTime

data class ToolWindowData(
    val time: DateTime,
    val action: ToolWindowAction,
    val activeWindow: String
)

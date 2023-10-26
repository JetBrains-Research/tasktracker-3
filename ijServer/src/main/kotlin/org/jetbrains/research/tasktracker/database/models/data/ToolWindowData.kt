package org.jetbrains.research.tasktracker.database.models.data

object ToolWindowData : DataTable() {
    val action = text("action")
    val activeWindow = text("active_window")
}

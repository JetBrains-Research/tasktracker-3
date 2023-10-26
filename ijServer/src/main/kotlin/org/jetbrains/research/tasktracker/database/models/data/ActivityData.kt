package org.jetbrains.research.tasktracker.database.models.data

object ActivityData : DataTable() {
    val type = text("type")
    val info = text("info")
    val selectedText = text("selected_text").nullable()
    val actionId = integer("action_id").nullable()
}
package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

object ToolWindowData : DataTable() {
    val action = text("action")
    val activeWindow = text("active_window")

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[action] = iterator.next()
        insertStatement[activeWindow] = iterator.next()
    }
}

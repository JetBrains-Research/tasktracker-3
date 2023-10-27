package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

object ActivityData : DataTable() {
    val type = text("type")
    val info = text("info")
    val selectedText = text("selected_text").nullable()
    val actionId = integer("action_id").nullable()

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[type] = iterator.next()
        insertStatement[info] = iterator.next()
        insertStatement[selectedText] = iterator.next()
        insertStatement[actionId] = iterator.next().toIntOrNull()
    }
}

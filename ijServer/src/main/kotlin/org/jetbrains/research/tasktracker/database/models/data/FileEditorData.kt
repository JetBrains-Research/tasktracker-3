package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

object FileEditorData : DataTable() {
    val action = text("action")
    val oldFile = text("old_file").nullable()
    val newFile = text("new_file").nullable()

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[action] = iterator.next()
        insertStatement[oldFile] = iterator.next()
        insertStatement[newFile] = iterator.next()
    }
}

package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

/**
 *
 * The table represents events of changing the active file in the file panel in the IDE.
 */
object FileEditorData : DataTable() {
    /**
     * An action type. Available types: OPEN, CLOSE, FOCUS.
     */
    val action = text("action")

    /**
     * The file from which the focus was switched.
     */
    val oldFile = text("old_file").nullable()

    /**
     * The file to which the focus was switched.
     */
    val newFile = text("new_file").nullable()

    init {
        uniqueIndex(date, action, oldFile, newFile)
    }

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[action] = iterator.next()
        insertStatement[oldFile] = iterator.next()
        insertStatement[newFile] = iterator.next()
    }
}

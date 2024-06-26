package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.UpdateBuilder

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

    override fun insertData(updateBuilder: UpdateBuilder<*>, iterator: Iterator<String>, researchId: Int) {
        updateBuilder[action] = iterator.next()
        updateBuilder[oldFile] = iterator.next()
        updateBuilder[newFile] = iterator.next()
    }
}

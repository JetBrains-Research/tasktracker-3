package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

/**
 * The table represents information about events with tool windows in IDE.
 */
object ToolWindowData : DataTable() {

    /**
     * The action that was captured. The following actions are currently available - OPENED, FOCUSED.
     */
    val action = text("action")

    /**
     * Active window name
     */
    val activeWindow = text("active_window")

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[action] = iterator.next()
        insertStatement[activeWindow] = iterator.next()
    }
}

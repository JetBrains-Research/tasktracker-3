package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.UpdateBuilder

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

    override fun insertData(updateBuilder: UpdateBuilder<*>, iterator: Iterator<String>, researchId: Int) {
        updateBuilder[action] = iterator.next()
        updateBuilder[activeWindow] = iterator.next()
    }
}

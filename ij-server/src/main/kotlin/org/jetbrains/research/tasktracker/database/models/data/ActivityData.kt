package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

/**
 * The table presenting all the activities that have occurred in the IDE.
 */
object ActivityData : DataTable() {
    /**
     * Activity type. Available types: Action, Execution, Shortcut, KeyPressed, KeyReleased
     */
    val type = text("type")

    /**
     * Information about the activity, depending on its type.
     * Example for Type.Action - 'Run'.
     * Example for Type.Shortcut - 'Meta 86'.
     */
    val info = text("info")

    /**
     * Selected text in the current opened file.
     */
    val selectedText = text("selected_text").nullable()

    /**
     * Activity ID as provided by the IDE.
     */
    val actionId = integer("action_id").nullable()

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[type] = iterator.next()
        insertStatement[info] = iterator.next()
        insertStatement[selectedText] = iterator.next()
        insertStatement[actionId] = iterator.next().toIntOrNull()
    }
}

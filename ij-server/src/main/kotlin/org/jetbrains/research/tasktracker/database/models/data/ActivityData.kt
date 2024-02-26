package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.UpdateBuilder

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
    val actionId = integer("action_id").nullable().default(-1)

    override fun insertData(updateBuilder: UpdateBuilder<*>, iterator: Iterator<String>, researchId: Int) {
        updateBuilder[type] = iterator.next()
        updateBuilder[info] = iterator.next()
        updateBuilder[selectedText] = iterator.next()
        updateBuilder[actionId] = iterator.next().toIntOrNull()
    }
}

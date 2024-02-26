package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * The table represents information about changes to the file used in the research.
 */
object DocumentData : DataTable() {
    val timestamp = long("timestamp")
    val filename = text("filename")
    val fileHashCode = integer("file_hash_code")
    val documentHashCode = integer("document_hash_code")
    val fragment = text("fragment")

    /**
     * The code written in the file at the moment.
     */
    val testMode = text("test_mode")

    override fun insertData(updateBuilder: UpdateBuilder<*>, iterator: Iterator<String>, researchId: Int) {
        updateBuilder[timestamp] = iterator.next().toLong()
        updateBuilder[filename] = iterator.next()
        updateBuilder[fileHashCode] = iterator.next().toInt()
        updateBuilder[documentHashCode] = iterator.next().toInt()
        updateBuilder[fragment] = iterator.next()
        updateBuilder[testMode] = iterator.next()
    }
}

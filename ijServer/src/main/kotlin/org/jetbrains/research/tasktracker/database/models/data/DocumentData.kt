package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

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

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[timestamp] = iterator.next().toLong()
        insertStatement[filename] = iterator.next()
        insertStatement[fileHashCode] = iterator.next().toInt()
        insertStatement[documentHashCode] = iterator.next().toInt()
        insertStatement[fragment] = iterator.next()
        insertStatement[testMode] = iterator.next()
    }
}

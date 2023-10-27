package org.jetbrains.research.tasktracker.database.models.data

object DocumentData : DataTable() {
    val timestamp = long("timestamp")
    val filename = text("filename")
    val fileHashCode = integer("file_hash_code")
    val documentHashCode = integer("document_hash_code")
    val fragment = text("fragment")
    val testMode = text("test_mode")
}

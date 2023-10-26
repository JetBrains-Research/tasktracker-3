package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

open class DataTable : IntIdTable() {
    val date = date("date")
}
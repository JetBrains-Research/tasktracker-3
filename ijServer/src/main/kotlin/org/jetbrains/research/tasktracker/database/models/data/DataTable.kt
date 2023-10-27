package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.research.tasktracker.database.models.Researches

open class DataTable : IntIdTable() {
    val researchId = reference("research_id", Researches).index()
    val date = date("date")
}

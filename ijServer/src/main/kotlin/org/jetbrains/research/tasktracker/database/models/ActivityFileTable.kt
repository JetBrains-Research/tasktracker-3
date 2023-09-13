package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.dao.id.IntIdTable

object ActivityFileTable: IntIdTable() {
    val name = varchar("name", 128)
    val researchId = integer("research_id").references(ResearchTable.id)
}
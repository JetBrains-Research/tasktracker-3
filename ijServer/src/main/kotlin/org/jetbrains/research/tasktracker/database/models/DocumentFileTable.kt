package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.dao.id.IntIdTable

object DocumentFileTable : IntIdTable() {
    val name = text("name")
    val researchId = integer("research_id").references(Researches.id)
}

package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object ResearchTable : Table() {
    val id = integer("id")
    val userId = integer("user_id")
    val name = text("name")

    override val primaryKey = PrimaryKey(id)
    fun maxUserId(): Int = transaction {
        slice(userId.max())
            .selectAll()
            .singleOrNull()
            ?.getOrNull(userId.max()) ?: 0
    }

    fun maxId(): Int = transaction {
        ResearchTable.slice(ResearchTable.id.max())
            .selectAll()
            .singleOrNull()
            ?.getOrNull(ResearchTable.id.max()) ?: 0
    }
}

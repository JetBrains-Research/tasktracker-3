package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.research.tasktracker.database.models.Researches
import java.time.OffsetDateTime

abstract class DataTable : IntIdTable() {
    val researchId = reference("research_id", Researches).index()
    val date = timestampWithTimeZone("date")

    protected abstract fun insertData(
        insertStatement: InsertStatement<Number>,
        iterator: Iterator<String>,
        researchId: Int
    )

    fun insertDefaultData(
        iterator: Iterator<String>,
        researchId: Int,
    ) {
        insert {
            it[this.researchId] = EntityID(researchId, Researches)
            it[date] = OffsetDateTime.parse(iterator.next())
            insertData(it, iterator, researchId)
        }
    }
}

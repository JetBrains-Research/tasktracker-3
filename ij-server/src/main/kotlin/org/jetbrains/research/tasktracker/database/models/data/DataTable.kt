package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.research.tasktracker.database.models.Researches
import java.time.OffsetDateTime

abstract class DataTable : IntIdTable() {
    val researchId = reference("research_id", Researches)
    val date = timestampWithTimeZone("date")

    protected abstract fun insertData(
        updateBuilder: UpdateBuilder<*>,
        iterator: Iterator<String>,
        researchId: Int
    )

    fun insertDefaultData(
        iterator: Iterator<String>,
        researchId: Int,
    ) {
        insertIgnore {
            it[this.researchId] = EntityID(researchId, Researches)
            it[date] = OffsetDateTime.parse(iterator.next())
            insertData(it, iterator, researchId)
        }
    }
}
